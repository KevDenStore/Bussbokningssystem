// Importera nödvändiga Java-paket

import java.util.Arrays;// Används för att sortera bokningar
import java.util.InputMismatchException;// Används för att hantera felaktig inmatning från användaren
import java.util.Scanner;

public class Bussbokningssystem {
    // Definiera konstanter för fönsterplatser, gångplatser, priser och totala platser
    static final int[] FONSTER_PLATSER = {1, 4, 5, 8, 9, 12, 13, 16, 17, 21}; // Platsnummer för fönsterplatser
    static final int[] GANG_PLATSER = {2, 3, 6, 7, 10, 11, 14, 15, 18, 19, 20};// Platsnummer för gångplatser
    static final double PRIS_VUXEN = 299.90;
    static final double PRIS_BARN = 149.90;
    static final double PRIS_PENSIONAR = 200.0;

    static final int TOTAL_PLATSER = 21;
    static String[] bokningar = new String[TOTAL_PLATSER]; // Array för att lagra bokningar på platserna

    public static void main(String[] args) {
            // Huvudmeny för bussbokningssystemet
        Scanner input = new Scanner(System.in);
        int val;
        do {
            System.out.println("\nMeny för bussbokning, välj alternativ med siffra 0-6:");
            System.out.println("1. Boka en plats för din färd");
            System.out.println("2. Hitta en bokning med personnummer eller namn så du kan se vilken plats hen sitter på");
            System.out.println("3. Ta bort en bokning med personnummer eller namn");
            System.out.println("4. Skriv ut sorterad lista av bokningar så du kan se vilka platser är upptagna av vilka personer");
            System.out.println("5. Beräkna vinsten av alla sålda biljetter");
            System.out.println("6. Visa lediga platser");            
            System.out.println("0. Avsluta programmet");
            System.out.print("Välj ett alternativ: ");
            val = input.nextInt();
                    // Använd en switch-sats för att hantera användarens val
            switch (val) {
                case 1 -> bokaPlats();
                case 2 -> hittaBokning();
                case 3 -> taBortBokning();
                case 4 -> skrivUtBokningar();
                case 5 -> System.out.println("Vinsten för alla bokningar är: " + beraknaVinst());
                case 6 -> ledigaPlatser();
                case 0 -> System.out.println("Programmet avslutas");
                default -> System.out.println("Felaktigt val, var snäll och försök igen med siffrorna 0-5");
            }
        } while (val != 0);
    }
// Funktionen bokaPlats() hanterar bokningsprocessen av en plats genom att begära och verifiera input från användaren
static void bokaPlats() {
    Scanner input = new Scanner(System.in);// skapar en ny Scanner-objekt som tar emot användarens inmatning från terminalen.
    System.out.print("Ange födelsedatum för resenär i detta formatet: (yyyymmdd): "); // skriver ut en instruktion för användaren att mata in födelsedatumet i rätt format.
    String fodelsedatum;
        // Hantera felaktig inmatning av födelsedatum
        // och sedan lägga till bokningsinformationen i bokningar-arrayen.
        // skapar en try-catch-block för att hantera felaktig inmatning av födelsedatumet och skriver ut ett felmeddelande om det skulle uppstå en InputMismatchException.
    try {
        fodelsedatum = input.next("[0-9]{8}"); // tar emot användarens inmatning och kontrollerar att den matchar mönstret [0-9]{8} som motsvarar åttasiffrigt heltal.
    } catch (InputMismatchException e) { //input mismatch om man skriver bokstav.
        System.out.println("Felaktig inmatning. Ange födelsedatumet som ett åttasiffrigt heltal (yyyymmdd).");
        return;
    }
    System.out.print("Ange förnamn: ");// skriver ut en instruktion för användaren att mata in sitt förnamn.
    String fornamn;
        try {
        fornamn = input.next("[a-zA-Z]+");// tar emot användarens inmatning och kontrollerar att den endast innehåller bokstäver.
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange namnet med bokstäver");
        return;
    }
    System.out.print("Ange efternamn: "); // skriver ut en instruktion för användaren att mata in sitt efternamn.
    String efternamn;
            try {
        efternamn = input.next("[a-zA-Z]+");
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange namnet med bokstäver");
        return;
    }
    System.out.print("Ange biologiskt kön med stor eller liten bokstav (M/K) m=man k=kvinna (detta påverkar inte priset för resenär): "); // skriver ut en instruktion för användaren att mata in sitt biologiska kön.
    String kon;
    try {
        kon = input.next("[MKmk]"); // tar emot användarens inmatning och kontrollerar att den endast är antingen 'M' eller 'K' i stora och små bokstäver.
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange könet som antingen 'M' eller 'K'.");
        return;
    }
    System.out.println("Välj en plats:");
    System.out.println("Fönsterplatser: " + platserToString(FONSTER_PLATSER));
    System.out.println("Gångplatser: " + platserToString(GANG_PLATSER));
    System.out.print("Ange platsnummer: ");
    try {
        int platsnummer = input.nextInt();
        if (platsFinns(platsnummer)) {
            System.out.println("Platsen är redan bokad");
        } else {
            String bokning = fodelsedatum + ";" + fornamn +";" + efternamn + ";" + kon + ";" + platsnummer;
            bokningar[platsnummer - 1] = bokning;
            System.out.println("Bokningen är registrerad");
        }
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange ett heltal som platsnummer.");
    }
}
// Funktionen hittaBokning() söker efter bokningar baserat på ett sökord (personnummer eller namn) och skriver ut bokningsinformationen
// för alla matchande bokningar.

static void hittaBokning() {
    Scanner input = new Scanner(System.in); // Skapar en Scanner-objekt för att ta emot input från användaren
    System.out.print("Ange personnummer eller resenärens för/efter/hela namn: ");// Ber användaren att ange ett sökord för att söka efter en bokning
    String sokord = input.next(); //fångar och läser in användarens val som en integer
    boolean hittad = false; // En variabel som används för att kontrollera om en bokning hittades eller inte
    int i = 0; // En räknare som används för att iterera genom bokningar-arrayen genom en interger
    while (i < bokningar.length) { // En loop som söker efter bokningar som matchar sökordet och skriver ut dem
        if (bokningar[i] != null && bokningar[i].contains(sokord)) {
            System.out.println("Platsnummer " + (i + 1) + ": " + bokningar[i]);
            hittad = true;
        }
        i++;
    }
    //om sökordet inte hittas 
    if (!hittad) {
        System.out.println("Ingen bokning hittades med sökordet: " + sokord);// Skriver ut ett meddelande om ingen bokning hittades som matchar sökordet
    }
}
// Funktionen taBortBokning() hanterar borttagning av bokning baserat på användarens val.
// Användaren kan välja att ta bort en bokning baserat på personnummer, namn eller platsnummer.

static void taBortBokning() {
    Scanner input = new Scanner(System.in);
    System.out.println("Vill du ta bort en bokning med (1) förnamn, (2) personnummer? eller (3) platsnummer");
    int val = input.nextInt(); // Läs in användarens val som en integer
    input.nextLine(); // Konsumera/Tar bort newline-character/newline-tecknet kvar i indata bufferten efter föregående indata


    switch (val) {// Börja en switch-sats baserat på användarens val
        case 1 -> {// Om användaren väljer alternativ 1 (sökning efter förnamn)
            System.out.print("Ange förnamn: ");
            String söknamn = input.nextLine();// Läs in användarens indata som en sträng
            for (int i = 0; i < bokningar.length; i++) {  // Loopa igenom alla bokningar i arrayen
                if (bokningar[i] != null) { // Om bokningen finns
                    String[] parts = bokningar[i].split(";");  // Dela upp bokningssträngen i delar baserat på semikolonerna
                    String fornamn = parts[1];
                    if (fornamn.equals(söknamn)) {// Om förnamnet matchar användarens inmatning
                        System.out.println("Bokning på plats " + (i + 1) + " togs bort: " + bokningar[i]); // Skriv ut bokningsinformationen som hittades
                        bokningar[i] = null;// Ta bort bokningen från arrayen genom att sätta den till null detta hjälper hålla räkning på alla lediga platser
                        return;// Avsluta metoden tillbaka till menyn

                    }
                }
            }
            System.out.println("Ingen bokning hittades med detta förnamn");
        }
        //resterande kod är lik det som skrevs i case 1 
        case 2 -> {
            System.out.print("Ange personnummer: ");
            String personnummer = input.nextLine();
            for (int i = 0; i < bokningar.length; i++) {
                if (bokningar[i] != null) {
                    String[] parts = bokningar[i].split(";");
                    String fodelsedatum = parts[0];
                    if (fodelsedatum.equals(personnummer)) {
                        System.out.println("Bokning på plats " + (i + 1) + " togs bort: " + bokningar[i]);
                        bokningar[i] = null;
                        return;
                    }
                }
            }
            System.out.println("Ingen bokning hittades med detta personnummer");
        }
        case 3 -> {
            System.out.print("Ange platsnummer: ");
            int platsnummer = input.nextInt();
            if (platsFinns(platsnummer)) {
                bokningar[platsnummer - 1] = null;
                System.out.println("Bokningen på plats " + platsnummer + " är borttagen");
            } else {
                System.out.println("Platsen är inte bokad");
            }
        }
        default -> System.out.println("Ogiltigt val"); //default switch för allt annat än 1,2,3 för en allmänt robust kod
    }
}


static void skrivUtBokningar() {
        sorteraBokningar();// Sortera bokningarna i bokningar-arrayen
System.out.println("Bokningar:"); // Skriv ut en rubrik för bokningarna som ska skrivas ut
for (int i = 0; i < bokningar.length; i++) { // Loopa igenom bokningar-arrayen och skriv ut bokningarna som finns
if (bokningar[i] != null) { // Om bokningen på index i i bokningar-arrayen inte är null, skriv ut bokningsinformationen
System.out.println("Platsnummer " + (i+1) + ": " + bokningar[i]); // Slut på metoden
}
}
}
// Funktionen sorteraBokningar() sorterar bokningar-arrayen i bokstavsordning baserat på efternamn och förnamn.
    // det är en statisk metod för att sortera bokningar i bokningar-arrayen
static void sorteraBokningar() {
    // Använda Arrays.sort metoden med en jämförelsefunktion i form av ett lambda-uttryck
    Arrays.sort(bokningar, (b1, b2) -> {
        if (b1 == null) { // Om b1 är null, ska den sorteras sist
            return 1;
        } else if (b2 == null) { // Om b2 är null, ska den sorteras först
            return -1;
        } else { // Dela upp bokningarna i delar med semikolon som separator
            String[] parts1 = b1.split(";");
            String[] parts2 = b2.split(";");
            // Skapa strängar med efternamn och förnamn för varje bokning
            String namn1 = parts1[2] + " " + parts1[1]; // Efternamn + Förnamn
            String namn2 = parts2[2] + " " + parts2[1]; // Efternamn + Förnamn
            // Jämför efternamn och förnamn för de två bokningarna
            return namn1.compareTo(namn2);
        }
    });
}

// Funktionen beraknaVinst() räknar ut total vinst för alla sålda biljetter genom att använda antalet passagerare i olika åldersgrupper och biljettpriserna.

    static double beraknaVinst() {
        int[] antal = countPassengers(); // Hämta antalet passagerare i olika kategorier med hjälp av countPassengers-metoden
        return (antal[0] * PRIS_VUXEN) + (antal[1] * PRIS_BARN) + (antal[2] * PRIS_PENSIONAR); 
    } // Beräkna vinsten genom rekursion, multiplicerar antalet passagerare i varje kategori med det aktuella priset för den kategorin och sedan addera resultaten för alla kategorier

// Funktionen countPassengers räknar antalet passagerare som har gjort en bokning
// Den loopar igenom alla bokningar i bokningar-arrayen och kontrollerar åldern på resenären
// Baserat på åldern ökar antalet räknade vuxna, barn eller pensionärer
    static int[] countPassengers() {
        // Funktionen returnerar en int-array med antalet räknade vuxna, barn och pensionärer i ordningen [antalVuxna, antalBarn, antalPensionarer]
        int antalVuxna = 0;
        int antalBarn = 0;
        int antalPensionarer = 0;

        for (String bokning : bokningar) { // Loopar igenom alla bokningar i bokningar-arrayen
            if (bokning != null) { // Kontrollerar att bokningen finns
                String[] parts = bokning.split(";"); // Delar upp bokningen i delar baserat på semikolon
                String fodelsedatum = parts[0]; // Hämtar födelsedatumet från bokningen
                int birthYear = Integer.parseInt(fodelsedatum.substring(0, 4)); // Plockar ut årtalet ur födelsedatumet
                int currentYear = java.time.LocalDate.now().getYear(); // Hämtar det aktuella året
                int age = currentYear - birthYear; // Beräknar resenärens ålder
                            if (age < 18) { // Om resenären är under 18 år räknas de som ett barn
                antalBarn++;
            } else if (age >= 65) { // Om resenären är 65 år eller äldre räknas de som en pensionär
                antalPensionarer++;
            } else { // Annars räknas de som en vuxen
                antalVuxna++;
            }
        }
    }

    return new int[]{antalVuxna, antalBarn, antalPensionarer};// Returnerar en int-array med antalet räknade vuxna, barn och pensionärer i ordningen [antalVuxna, antalBarn, antalPensionarer]
}
// Funktionen platsFinns() returnerar true om en plats är bokad och false annars.

static boolean platsFinns(int platsnummer) {
        return bokningar[platsnummer - 1] != null; // Returnerar true om bokningar arrayen har en bokning på platsnummer-1, annars false.
}
// Funktionen platserToString() konverterar en int-array med platser till en sträng som kan skrivas ut i konsollen.
static String platserToString(int[] platser) {
String s = ""; // Skapa en tom sträng för att lagra platsnumren
for (int i = 0; i < platser.length; i++) { // Loopa igenom platserna i arrayen
s += platser[i];     // Lägg till platsnumret till strängen
if (i < platser.length - 1) {     // Om det inte är sista platsnumret, lägg till ett kommaseparerat mellanrum
s += ", ";
}
}
return s; // Returnera den skapade strängen
}
// Funktionen ledigaPlatser() räknar antalet lediga platser (fönsterplatser och gångplatser) och skriver ut resultatet i konsollen.
    static void ledigaPlatser() {
        int availableWindowSeats = countAvailableSeats(FONSTER_PLATSER);  //Räkna antalet lediga fönsterplatser genom att anropa metoden 'countAvailableSeats'
        int availableAisleSeats = countAvailableSeats(GANG_PLATSER);  //Räkna antalet lediga gångplatser genom att anropa metoden 'countAvailableSeats'

        System.out.println("Antal lediga fönsterplatser: " + availableWindowSeats); //Skriv ut antalet lediga fönsterplatser
        System.out.println("Antal lediga gångplatser: " + availableAisleSeats); //Skriv ut antalet lediga gångplatser
    }
//Metoden 'countAvailableSeats' tar in en array med platser och räknar antalet lediga platser genom att loopa igenom arrayen och anropa 'platsFinns' metoden.
    static int countAvailableSeats(int[] seatArray) { //Skapa en variabel för att räkna antalet lediga platser
        int availableSeats = 0;  //Skapa en variabel för att räkna antalet lediga platser
        for (int seatNumber : seatArray) {  //Loopa igenom alla platser i arrayen
            if (!platsFinns(seatNumber)) {
                availableSeats++;
            }
        }
        return availableSeats;//Returnera antalet lediga platser
    }
}