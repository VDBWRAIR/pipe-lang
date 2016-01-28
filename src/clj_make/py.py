import os
import re
import sh
from toolz import compose
import tempfile
from toolz.dicttoolz import keyfilter
from functools import partial
from fn import _
import edn_format
import networkx
from itertools import repeat

G = networkx.DiGraph()
#G.add_edges_from(
res = edn_format.loads(open("tree.edn").read())
tasks = map(make_task, find(res, 'rule'))
X=_
def fix_pack(x):  #dict inc.(deps,targets,actions)
    dep, target, actions = x['file_deps'], x['targets'], x['actions']
    data = {'actions' : actions, 'complete' : False}
    if dep.startswith("?"):
        data.update(optional=True)
    return dep, target, data

packs = list(itertools.starmap(compose(list, itertools.product), tasks))
G.add_edges_from(map(fix_pack, itertools.chain(*itertools.chain(packs))))
order = networkx.topological_sort(G)
start_nodes = set(map(X[0], G.edges())) - set(map(X[1], G.edges())) # these nodes have no producing rules. they should exist prior to starting the pipeline.
class Edge(object):
    fields = ['deps', 'edges', 'metadata', 'actions', 'graph']
    def __init__(deps, edges, metadata, actions, graph):
        self.deps, self.edges, self.metadata, self.actions, self.graph = deps, edges, metadata, actions, graph

def run_io_handler(edge):
    for f in edge.actions:
        print str(f)
        f()

def dry_run_handler(edge):
    for f in edge.actions:
        print '#%s' % str(f)
def process_graph(G, handler):
    newtate = G
    for goal in order:
        if goal in nx.get_node_attributes(newstate, 'complete'):
            continue
        newstate = build_target(newstate, handler, goal)
    return newstate
#instead of using graph, could use top sorte and then
# sorted(tasks, key = order.indexOf)
def build_target(ingraph, handler, goal):
    G = ingraph.copy()
    deps = G.predecesors(goal)
    edges  = G.in_edges(goal)
    metadata = G[deps[0]][goal]
    actions = metadata['actions']
    other_targets = map(_[1], filter(_['actions'] == actions, G.in_edges)
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


@contract(rule='list(tuple(str, str))) #(dep, target)
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



