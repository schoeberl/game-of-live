/*
 * Test a single cell.
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */

package gol

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class GoLTest extends AnyFlatSpec with ChiselScalatestTester {

  "GoL" should "play a simple pattern" in {
    val start = Array(Array(0, 0, 0), Array(1, 1, 1), Array(0, 0, 0))

    test(new World(start)) { dut =>
      val n = start.length

      def printWorld(): Unit = {
        def field(y: Int, x: Int) = if (dut.io.out(y)(x).peek().litToBoolean) 1 else 0

        for (y <- 0 until n) {
          for (x <- 0 until n ) {
            print(field(y, x) + " ")
          }
          println()
        }
        println()
      }

      for (i <- 0 until 4) {
        printWorld()
        dut.clock.step()
      }
    }
  }

  "GoL" should "cosimulate" in {

    val start = Util.getRandomWorld(10)
    val gol = new GoL(start)

    test(new World(start)) { dut =>
      val n = start.length
      def field(y: Int, x: Int) = if (dut.io.out(y)(x).peek().litToBoolean) 1 else 0

      for (i <- 0 until 10) {
        for (y <- 0 until n) {
          for (x <- 0 until n ) {
            // print(field(y, x) + ":" + gol.getVal(y, x) + " ")
            assert(field(y, x) == gol.getVal(y, x))
          }
          // println()
        }
        dut.clock.step()
        gol.step()
        // println()
      }
    }
  }
}
