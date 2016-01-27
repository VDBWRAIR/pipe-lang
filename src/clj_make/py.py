import os
import re
import sh
from toolz import compose
import tempfile
from toolz.dicttoolz import keyfilter
from functools import partial
from fn import _

#res = edn_format.loads(open("tree.edn").read())

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



