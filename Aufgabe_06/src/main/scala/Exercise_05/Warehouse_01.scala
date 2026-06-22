package Exercise_05

import cats.effect.{IO, IOApp, Ref}
import cats.implicits._
import scala.concurrent.duration._

object Warehouse_01 extends IOApp.Simple {

  case class Warehouse(stock: Ref[IO, Int]) {

    // Thread-sicheres Abbuchen vom Lagerbestand
    def buyProduct(name: String, amount: Int): IO[Unit] = {
      stock.modify { current =>
        if (current >= amount) {
          (current - amount, IO.println(s"$name hat $amount Stück gekauft. Neuer Bestand: ${current - amount}"))
        } else {
          (current, IO.println(s"$name wollte $amount Stück kaufen, aber es sind nur noch $current da!"))
        }
      }.flatten
    }

    // Thread-sicheres Auffüllen des Lagerbestands
    def reorderProducts(amount: Int): IO[Unit] = {
      stock.update(_ + amount) >> IO.println(s"Lager um $amount Stück aufgefüllt.")
    }
  }

  def run: IO[Unit] = {
    for {
      // Initialer Lagerbestand: 10 Stück
      initialStock <- Ref.of[IO, Int](10)
      warehouse = Warehouse(initialStock)

      _ <- IO.println("--- Start Lagerverwaltung Simulation ---")

      // Simulation von parallelen Käufen (wie im Beispiel beschrieben)
      // Thread B möchte 2 Stück, Thread A möchte 1 Stück, und viele andere...
      buyers = List(
        warehouse.buyProduct("Kunde A", 1),
        warehouse.buyProduct("Kunde B", 2),
        warehouse.buyProduct("Kunde C", 5),
        warehouse.buyProduct("Kunde D", 3), // Sollte fehlschlagen, wenn A, B, C zuerst dran waren
        warehouse.reorderProducts(5),      // Auffüllen zwischendurch
        warehouse.buyProduct("Kunde E", 4)
      )

      // Alle Aktionen parallel ausführen
      _ <- buyers.parSequence

      finalStock <- initialStock.get
      _ <- IO.println(s"--- Simulation beendet. Finaler Lagerbestand: $finalStock ---")
    } yield ()
  }
}
