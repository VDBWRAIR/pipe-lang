import os
import re
import sh
from collections import defaultdict
import toolz
from toolz import compose
import tempfile
from toolz.dicttoolz import keyfilter
from functools import partial
from fn import _
import edn_format
import networkx
from itertools import repeat
import itertools

#G = networkx.DiGraph()
res = edn_format.loads(open("tree.edn").read())
X=_

def flatten(x):
    result = []
    for el in x:
        if hasattr(el, "__iter__") and not isinstance(el, basestring):
            result.extend(flatten(el))
        else:
            result.append(el)
    return result

def find(xs, k, k2=None):
    if type(xs) != list:
        res = xs if xs[0].name == k else None
    else: res = filter(lambda x: False if type(x) != list else x[0].name == k, xs)
    if k2: return filter(lambda x: find(x, k2), res)
    else: return res or filter(lambda x: find(x, k), res)

contents = partial(map, _[1:])
get_contents = lambda xs: filter(lambda x: type(x) == unicode, flatten(xs))
get_keys = lambda d, xs: keyfilter(xs.__contains__, d)

def make_task(rule):
    ruleHeader = find(rule, 'ruleHeader')[0]
    get = compose(get_contents, contents, partial(find, ruleHeader)) 
    file_deps = get('dependency') 
    # how handle task_deps? discriminate file from non-file task?
    task_deps = get('orderDependencies')
    targets = get('target')
    variables = {
     "$OUT"  : targets[0],
     "$IN"   : file_deps[0],
     "$OUTS" : targets,
     "$INS"  : file_deps }
    def expand_vars(s):
        pattern = re.compile('|'.join(variables.keys())) 
        return pattern.sub(lambda x: variables[x.group()], s) 
    def make_action(rule):
        interpreter = 'python' if get('pythonBlock')  else 'bash'
        codelines = get(get('%sBlock' % interpreter), 'codeline') 
        with tempfile.NamedTemporaryFile(delete=False) as f:
            script_name = f.name 
            if interpreter == 'python':
                codelines = imports + codelines 
            codelines = map(expand_vars, codelines)
            f.writelines(codelines)
        runCmd = getattr(sh, interpreter).bake(script_name)
        return runCmd 
    actions = [make_action(rule)]
    task = get_keys(locals(), ['file_deps', 'actions', 'targets'])
    return task

tasks = map(make_task, find(res, 'rule'))
def ffirst(pred, seq):
    try:
        return next(itertools.ifilter(pred, seq))
    except:
        return None 
class Bunch(defaultdict):
    __getattr__ = dict.__getitem__
def options(): return Bunch(options)
opts = options()
opts.bwa.threads = 1
opts.cutadapt.whatever = "w/e"
#
#exec java -classpath /usr/share/maven-repo/org/scala-lang/scala-library/2.9.2/scala-library-2.9.2.jar:/home/michaelpanciera/anaconda/lib/python2.7/site-packages/jep/jep-3.4.3.jar:/home/michaelpanciera/.ivy2/local/com.github.marianobarrios/dregex_2.11/0.2-SNAPSHOT/jars/dregex_2.11.jar -Djava.library.path="$jni_path" jep.Run foo.py
# blows up with dregex :(
# build dregex using sbt assembly
# $ jython
# $ >>> import sys; sys.path.append( /path/to/dregex/jar
# call scala like you would call it from java
#java -classpath /home/michaelpanciera/anaconda/lib/python2.7/site-packages/jep/jep-3.4.3.jar:/usr/share/maven-repo/org/scala-lang/scala-library/2.9.2/scala-library-2.9.2.jar -Djava.library.path="$jni_path" jep.Run foo.py

#class Bunch(dict):
#    __getattr__ = dict.__getitem__
class Job(Bunch):
    def __init__(self, targets, deps, actions):
        self.targets, self.deps, self.actions =  targets, deps, actions

#~testQuick
#~compile
#spacemacs, w/ emacs-ensime (see emacs-ensime gitter)
#sys.path.append("/home/michaelpanciera/clj-make/dregex/target/scala-2.11/dregex-assembly-0.2-SNAPSHOT.jar") 
#  r = dregex.Regex#compile(".*\\.txt")

def topological_sort(tasks, target):
    job = ffirst(lambda x: target in x['targets'], tasks) 
    if job:
        file_deps = job['file_deps']
        job_deps = filter(bool, itertools.chain(*map(partial(top_sort, tasks), file_deps)))
        return job_deps + [job]
    return [] 
def process_job(handler, job):
    handler(job)
def run_io_handler(edge):
    for f in edge.actions:
        print str(f)
        f()

def dry_run_handler(edge):
    for f in edge.actions:
        print '#%s' % str(f)
job_sort = compose(toolz.itertoolz.unique, topological_sort)
run_graph_io = partial(map, partial(process_job, run_io_handler))
dry_run = partial(map, partial(process_job, dry_run_handler))

# the advantage of an actual graph representation is easy top-sort, visualization type stuff,
# edge metadata
def fix_pack(x):  #dict inc.(deps,targets,actions)
    dep, target, actions = x['file_deps'], x['targets'], x['actions']
    data = {'actions' : actions, 'complete' : False}
    #if dep.startswith("?"):
    #    data.update(optional=True)
    return dep, target, data

#packs = list(itertools.starmap(compose(list, itertools.product), tasks))
#G.add_edges_from(map(fix_pack, itertools.chain(*itertools.chain(packs))))
#order = networkx.topological_sort(G)
#start_nodes = set(map(X[0], G.edges())) - set(map(X[1], G.edges())) # these nodes have no producing rules. they should exist prior to starting the pipeline.
class Edge(object):
    fields = ['deps', 'edges', 'metadata', 'actions', 'graph']
    def __init__(deps, edges, metadata, actions, graph):
        self.deps, self.edges, self.metadata, self.actions, self.graph = deps, edges, metadata, actions, graph

def process_graph(G, handler):
    newtate = G
    for goal in order:
        if goal in nx.get_node_attributes(newstate, 'complete'):
            continue
        newstate = build_target(newstate, handler, goal)
    return newstate
#instead of using graph, could use top sorte and then
# sorted(tasks, key = order.indexOf)

#sorted(tasks, key = compose(order.index, 
def build_target(ingraph, handler, goal):
    G = ingraph.copy()
    deps = G.predecesors(goal)
    edges  = G.in_edges(goal)
    metadata = G[deps[0]][goal]
    actions = metadata['actions']
    other_targets = map(_[1], filter(_['actions'] == actions, G.in_edges))
    runnable = Edge(deps, edges, metadata, actions, G)
    handler(runnable) # run the process
    now_complete = dict(zip(other_targets + goal, repeat(True)))
    nx.set_node_attributes(G, 'complete', now_complete)
    return G
    #actions = nx.get_edge_attributes(G, 'actions')[edges[0]]
    # assert that files were created 
    # run the job which would produce that node.
    # # -> the job is the one where X[1] contains the node
    # remove G.predecessors(node), but not those nodes' successors, which are needed for later. or mark them as completed.
    # remove that job from the job list
    
#networx.has_path



#@contract(rule='list(tuple(str, str))): #(dep, target)
def annotate_rule((dep, target)): # should include order dep?
    metadata = None
    if dep.startswith("?"):
        metadata = {"optional" : True}
    return (dep, target, metadata)




def check_imports_and_execs(raw_tree):
    tree = raw_tree[1:]
    get = compose(contents, partial(find, tree))
    imports = get('import', 'module')
    execs = get('requires', 'module')
    execs = filter(lambda x: type(x) == unicode, flatten(execs))
    imports = filter(lambda x: type(x) == unicode, flatten(imports))
    return imports
    for e in execs: check_exec(e)
    for i in imports: check_import(i)

def check_import(name):
    if not sh.python("-c", "import %s" % name):
        raise ImportError("Import %s not found in python (found in %s)." % (name, sh.which(python)))
    
def check_exec(exec_name):
    if not sh.which(exec_name):
        raise ValueError("executable %s not found in path." % exec_name)




