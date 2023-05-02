import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bussbokningssystem {
    static final int[] FONSTER_PLATSER = {1, 4, 5, 8, 9, 12, 13, 16, 17, 21};
    static final int[] GANG_PLATSER = {2, 3, 6, 7, 10, 11, 14, 15, 18, 19, 20};
    static final double PRIS_VUXEN = 299.90;
    static final double PRIS_BARN = 149.90;
    static final double PRIS_PENSIONAR = 200.0;

    static final int TOTAL_PLATSER = 21;
    static String[] bokningar = new String[TOTAL_PLATSER];

    public static void main(String[] args) {
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

static void bokaPlats() {
    Scanner input = new Scanner(System.in);
    System.out.print("Ange födelsedatum för resenär i detta formatet: (yyyymmdd): ");
    String fodelsedatum;
    try {
        fodelsedatum = input.next("[0-9]{8}");
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange födelsedatumet som ett åttasiffrigt heltal (yyyymmdd).");
        return;
    }
    System.out.print("Ange förnamn: ");
    String fornamn;
        try {
        fornamn = input.next("[a-zA-Z]+");
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange namnet med bokstäver");
        return;
    }
    System.out.print("Ange efternamn: ");
    String efternamn;
            try {
        efternamn = input.next("[a-zA-Z]+");
    } catch (InputMismatchException e) {
        System.out.println("Felaktig inmatning. Ange namnet med bokstäver");
        return;
    }
    System.out.print("Ange biologiskt kön med stor eller liten bokstav (M/K) m=man k=kvinna (detta påverkar inte priset för resenär): ");
    String kon;
    try {
        kon = input.next("[MKmk]");
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

static void hittaBokning() {
    Scanner input = new Scanner(System.in);
    System.out.print("Ange personnummer eller resenärens för/efter/hela namn: ");
    String sokord = input.next();
    boolean hittad = false;
    int i = 0;
    while (i < bokningar.length) {
        if (bokningar[i] != null && bokningar[i].contains(sokord)) {
            System.out.println("Platsnummer " + (i + 1) + ": " + bokningar[i]);
            hittad = true;
        }
        i++;
    }
    if (!hittad) {
        System.out.println("Ingen bokning hittades med sökordet: " + sokord);
    }
}

static void taBortBokning() {
    Scanner input = new Scanner(System.in);
    System.out.println("Vill du ta bort en bokning med (1) förnamn, (2) personnummer? eller (3) platsnummer");
    int val = input.nextInt();
    input.nextLine(); // Tar bort newline character från förra inputen

    switch (val) {
        case 1 -> {
            System.out.print("Ange förnamn: ");
            String söknamn = input.nextLine();
            for (int i = 0; i < bokningar.length; i++) {
                if (bokningar[i] != null) {
                    String[] parts = bokningar[i].split(";");
                    String fornamn = parts[1];
                    if (fornamn.equals(söknamn)) {
                        System.out.println("Bokning på plats " + (i + 1) + " togs bort: " + bokningar[i]);
                        bokningar[i] = null;
                        return;
                    }
                }
            }
            System.out.println("Ingen bokning hittades med detta förnamn");
        }
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
        default -> System.out.println("Ogiltigt val");
    }
}


static void skrivUtBokningar() {
        sorteraBokningar();
System.out.println("Bokningar:");
for (int i = 0; i < bokningar.length; i++) {
if (bokningar[i] != null) {
System.out.println("Platsnummer " + (i+1) + ": " + bokningar[i]);
}
}
}
static void sorteraBokningar() {
    Arrays.sort(bokningar, (b1, b2) -> {
        if (b1 == null) {
            return 1;
        } else if (b2 == null) {
            return -1;
        } else {
            String[] parts1 = b1.split(";");
            String[] parts2 = b2.split(";");
            String namn1 = parts1[2] + " " + parts1[1]; // Efternamn + Förnamn
            String namn2 = parts2[2] + " " + parts2[1]; // Efternamn + Förnamn
            return namn1.compareTo(namn2);
        }
    });
}


    static double beraknaVinst() {
        int[] antal = countPassengers();
        return (antal[0] * PRIS_VUXEN) + (antal[1] * PRIS_BARN) + (antal[2] * PRIS_PENSIONAR);
    }

    static int[] countPassengers() {
        int antalVuxna = 0;
        int antalBarn = 0;
        int antalPensionarer = 0;

        for (String bokning : bokningar) {
            if (bokning != null) {
                String[] parts = bokning.split(";");
                String fodelsedatum = parts[0];
                int birthYear = Integer.parseInt(fodelsedatum.substring(0, 4));
                int currentYear = java.time.LocalDate.now().getYear();
                int age = currentYear - birthYear;
                            if (age < 18) {
                antalBarn++;
            } else if (age >= 65) {
                antalPensionarer++;
            } else {
                antalVuxna++;
            }
        }
    }

    return new int[]{antalVuxna, antalBarn, antalPensionarer};
}

static boolean platsFinns(int platsnummer) {
        return bokningar[platsnummer - 1] != null;
}

static String platserToString(int[] platser) {
String s = "";
for (int i = 0; i < platser.length; i++) {
s += platser[i];
if (i < platser.length - 1) {
s += ", ";
}
}
return s;
}
    static void ledigaPlatser() {
        int availableWindowSeats = countAvailableSeats(FONSTER_PLATSER);
        int availableAisleSeats = countAvailableSeats(GANG_PLATSER);

        System.out.println("Antal lediga fönsterplatser: " + availableWindowSeats);
        System.out.println("Antal lediga gångplatser: " + availableAisleSeats);
    }

    static int countAvailableSeats(int[] seatArray) {
        int availableSeats = 0;
        for (int seatNumber : seatArray) {
            if (!platsFinns(seatNumber)) {
                availableSeats++;
            }
        }
        return availableSeats;
    }
}