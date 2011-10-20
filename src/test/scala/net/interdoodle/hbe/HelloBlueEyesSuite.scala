package net.interdoodle.hbe

import domain.{Monkey, LetterProbabilities}
import org.scalatest.FunSuite


/** @see http://www.scalatest.org/scaladoc/1.6.1/#org.scalatest.FunSuite */
class HelloBlueEyesSuite extends FunSuite {

  test("generatePage") {
    /** Rough character frequency approximation */
    val document = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"*5 +
      "abcdefghijklmnopqrstuvwxyz"*25 +
      "0123456789"*2 +
      "`~!@#$%^&*()_-+={[}]|\\\"':;<,>.?/"
    val letterProbability = new LetterProbabilities()
    
    letterProbability.add(document)
    letterProbability.computeValues()
    assert(letterProbability.size==94)

    val monkey = new Monkey(letterProbability)
    val page = monkey.generatePage
    // todo write more tests and Monkey business logic
  }


  test("LetterProbabilities") {
    try {
      var lps = new LetterProbabilities()

      lps.add('a')
      assert(lps.size===1)
      lps.computeValues()
      assert(lps.normalizedValues.size===1)
      assert(lps.normalizedValues.head._1==='a')
      assert(lps.normalizedValues.head._2===1.0)
      assert(lps.normalizedValues.last._1==='a')
      assert(lps.normalizedValues.last._2===1.0)
      assert(lps.continuousValues.size===1)
      assert(lps.continuousValues.head._1==='a')
      assert(lps.continuousValues.head._2===1.0)
      assert(lps.letter(-123)==='a')
      assert(lps.letter(0)==='a')
      assert(lps.letter(0.2)==='a')
      assert(lps.letter(1)==='a')
      assert(lps.letter(123)==='a')

      lps.add('b')
      assert(lps.size==2)
      lps.computeValues()
      assert(lps.normalizedValues.size===2)
      assert(lps.normalizedValues.head._1==='a')
      assert(lps.normalizedValues.head._2===0.5)
      assert(lps.normalizedValues.last._1==='b')
      assert(lps.normalizedValues.last._2===0.5)
      assert(lps.continuousValues.size===2)
      assert(lps.continuousValues.head._1==='a')
      assert(lps.continuousValues.head._2===0.5)
      assert(lps.continuousValues.last._1==='b')
      assert(lps.continuousValues.last._2===1.0)
      assert(lps.letter(-123)==='a')
      assert(lps.letter(0)==='a')
      assert(lps.letter(0.2)==='a')
      assert(lps.letter(0.5)==='a')
      assert(lps.letter(0.6)==='b')
      assert(lps.letter(1)==='b')
      assert(lps.letter(123)==='b')

      lps.add("xyz")
      assert(lps.size==5)
      lps.computeValues()
      assert(lps.normalizedValues.size===5)
      assert(lps.normalizedValues.head._1==='a')
      assert(lps.normalizedValues.head._2===0.2)
      assert(lps.normalizedValues.last._1==='z')
      assert(lps.normalizedValues.last._2===0.2)
      assert(lps.continuousValues.size===5)
      assert(lps.continuousValues.head._1==='a')
      assert(lps.continuousValues.head._2===0.2)
      assert(lps.continuousValues.last._1==='z')
      assert(lps.continuousValues.last._2===1.0)
      assert(lps.letter(-123)==='a')
      assert(lps.letter(0)==='a')
      assert(lps.letter(0.2)==='a')
      assert(lps.letter(0.5)==='x')
      assert(lps.letter(0.6)==='x')
      assert(lps.letter(1)==='z')
      assert(lps.letter(123)==='z')

      //println(lps.normalizedValues)
      //println(lps.continuousValues)
    } catch {
      case e: Exception => println(e)
    }
  }
}