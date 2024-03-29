package dregex

import dregex.impl.RegexParser
import dregex.impl.Dfa
import dregex.impl.NormTree

/**
 * A regular expression, ready to be tested against strings, or to take part in an operation against another.
 * Internally, instances of this type have a DFA (Deterministic Finite Automaton).
 */
trait Regex {

  def dfa: Dfa
  def universe: Universe

  private def checkUniverse(other: Regex): Unit = {
    if (other.universe != universe)
      throw new Exception("cannot make operations between regex from different universes")
  }
  
  /**
   * Return whether a string is matched by the regular expression (i.e. whether the string is included in the language 
   * generated by the expression). 
   * As the match is done using a DFA, its complexity is O(n), where n is the length of the string. It is constant
   * with respect to the length of the expression.
   */
  def matches(string: String): Boolean = {
    val (result, _) = matchAndReport(string)
    result
  }
  
  /**
   * Similar to method [[matches]], except that also return how many characters were successfully matched in case of
   * failure.
   */
  def matchAndReport(string: String): (Boolean, Int) = {
    val genDfa = dfa.impl
    var current = genDfa.initial
    var i = 0
    for (char <- string) {
      val currentTrans = genDfa.transitions.getOrElse(current, Map())
      val litChar = NormTree.Lit(char)
      val effChar = if (universe.alphabet.contains(litChar))
        litChar
      else
        NormTree.Other
      current = currentTrans.get(effChar) match {
        case Some(newState) => newState
        case None => return (false, i)
      }
      i += 1
    }
    (genDfa.accepting.contains(current), i)
  }

  /**
   * Intersect this regular expression with another. The resulting expression will match the strings that are
   * matched by the operands, and only those. Intersections take O(n*m) time, where n and m are the number of states of
   * the DFA of the operands.
   */
  def intersect(other: Regex): Regex = {
    checkUniverse(other)
    new SynteticRegex(dfa intersect other.dfa, universe)
  }
  
  /**
   * Subtract other regular expression from this one. The resulting expression will match the strings that are
   * matched this expression and are not matched by the other, and only those. Differences take O(n*m) time, where n 
   * and m are the number of states of the DFA of the operands.
   */
  def diff(other: Regex): Regex = {
    checkUniverse(other)
    new SynteticRegex(dfa diff other.dfa, universe)
  }

  def findRelations(func: (Regex, Regex) => Boolean, xs: Seq[String]): Seq[(String, String)] = { 
    //TODO: make prettier and/or more efficient
    val rs = Regex compile xs
    val _perms = rs.combinations(2) ++ rs.combinations(2).map(_.reverse)
    val perms = _perms.map({case (x :: y :: Nil ) => (x, y)} ) 
    perms.filter( {case ((_, r1), (_, r2)) => func(r1, r2) }).map( {case ((s1, _), (s2, _)) => (s1, s2) }) toSeq
  }

  def findSubsetRelations(xs: Seq[String]): Seq[(String, String)] =
      findRelations(isSubsetOf _, xs)

  def findProperSubsetRelations(xs: Seq[String]): Seq[(String, String)] =
      findRelations(isProperSubsetOf _, xs)

  def isSubsetOf(other: Regex): Boolean = {
      checkUniverse(other)
      !(this diff other matchesAnything)
  }
  def isProperSubsetOf(other: Regex): Boolean =  {
      checkUniverse(other) 
      (this isSubsetOf other) && (other diff this matchesAnything)
  
  
  /**
   * Unite this regular expression with another. The resulting expression will match the strings that are matched by 
   * either of the operands, and only those. Unions take O(n*m) time, where n and m are the number of states of the DFA 
   * of the operands.
   */
  def union(other: Regex): Regex = {
    checkUniverse(other)
    new SynteticRegex(dfa union other.dfa, universe)
  }
  
  /**
   * Return whether this expression matches at least one string in common with another. Intersections take O(n*m) time, 
   * where n and m are the number of states of the DFA of the operands.
   */
  def doIntersect(other: Regex): Boolean = intersect(other).matchesAnything()

  /**
   * Return whether this regular expression is equivalent to other. Two regular expressions are equivalent if they
   * match exactly the same set of strings. This operation takes O(n*m) time, where n and m are the number of states of 
   * the DFA of the operands.
   */
  def equiv(other: Regex): Boolean = {
    checkUniverse(other)
    !(dfa diff other.dfa).matchesAnything() && !(other.dfa diff dfa).matchesAnything()
  }

  /**
   * Return whether this regular expression matches anything. Note that the empty string is a valid match.
   */
  def matchesAnything() = dfa.matchesAnything()

}

object Regex {

  def parse(regex: String): ParsedRegex = new ParsedRegex(RegexParser.parse(regex))
  
  def compile(regex: String): CompiledRegex = {
    val tree = parse(regex)
    new CompiledRegex(tree, new Universe(Seq(tree)))
  }

  def compileParsed(tree: ParsedRegex, universe: Universe): CompiledRegex = {
    new CompiledRegex(tree, universe)
  }
  
  def compile(regexs: Seq[String]): Seq[(String, CompiledRegex)] = {
    val trees = regexs.map(r => (r, parse(r)))
    val universe = new Universe(trees.unzip._2)
    for ((regex, tree) <- trees) yield regex -> new CompiledRegex(tree, universe)
  }
  
  /**
   * Create a regular expression that does not match anything. Note that that is different from matching the empty 
   * string. Despite the theoretical equivalence of automata and regular expressions, in practice there is no regular 
   * expression that does not match anything.
   */
  def nullRegex(u: Universe) = new SynteticRegex(Dfa.NothingDfa, u)

}
