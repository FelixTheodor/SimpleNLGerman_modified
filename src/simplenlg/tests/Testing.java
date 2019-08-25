package simplenlg.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testing {

    //yellow output if the test was right
    public static final String gr = "\u001B[32m";

    @Test
    public void testSentence1() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Er glaubt, das Buch nicht zu verstehen.", tester.sent1());
        System.out.println(gr+tester.sent1());
    }

    @Test
    public void testSentence2() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Die Frau versucht, den Mann nicht zu hören.", tester.sent2());
        System.out.println(tester.sent2());
    }

    @Test
    public void testSentence3() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Ich überlege, nach London zu fahren.", tester.sent3());
        System.out.println(tester.sent3());
    }

    @Test
    public void testSentence4() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Ich empfehle Anna, zu bleiben.", tester.sent4());
        System.out.println(tester.sent4());
    }

    @Test
    public void testSentence5() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Karl schenkt Anna die Möglichkeit, nicht zu sterben.", tester.sent5());
        System.out.println(tester.sent5());
    }

    @Test
    public void testSentence6() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Aufzuhören fällt ihr nicht schwer.", tester.sent6());
        System.out.println(tester.sent6());
    }

    @Test
    public void testSentence7() {
        TestMain tester = new TestMain();

        // assert statements
        assertEquals("Die Möglichkeit, nicht zu sterben, ist fantastisch.", tester.sent7());
        System.out.println(tester.sent7());
    }

    @Test
    public void testSentence8() {
        TestMain tester = new TestMain();

        String erg = tester.sent8();

        // assert statements
        assertEquals("Der Mann glaubt, zu schweben.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence9() {
        TestMain tester = new TestMain();

        String erg = tester.sent9();

        // assert statements
        assertEquals("Die Frau hofft, dieses Mal nicht zu verlieren.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence10() {
        TestMain tester = new TestMain();

        String erg = tester.sent10();

        // assert statements
        assertEquals("Es ist wichtig, zuzuhören.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence11() {
        TestMain tester = new TestMain();

        String erg = tester.sent11();

        // assert statements
        assertEquals("Du hast zu folgen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence12() {
        TestMain tester = new TestMain();

        String erg = tester.sent12();

        // assert statements
        assertEquals("Du hast dich zu unterwerfen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence13() {
        TestMain tester = new TestMain();

        String erg = tester.sent13();

        // assert statements
        assertEquals("Er kam, um zu helfen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence14() {
        TestMain tester = new TestMain();

        String erg = tester.sent14();

        // assert statements
        assertEquals("Er blieb, ohne sich zu regen, da.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence15() {
        TestMain tester = new TestMain();

        String erg = tester.sent15();

        // assert statements
        assertEquals("Er blieb da, ohne zu blinzeln.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence16() {
        TestMain tester = new TestMain();

        String erg = tester.sent16();

        // assert statements
        assertEquals("Er las, anstatt zu helfen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence17() {
        TestMain tester = new TestMain();

        String erg = tester.sent17();

        // assert statements
        assertEquals("Die Frau wünscht, am Hotel anzukommen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence18() {
        TestMain tester = new TestMain();

        String erg = tester.sent18();

        // assert statements
        assertEquals("Den Ball flachzuhalten ist das Wichtigste.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence19() {
        TestMain tester = new TestMain();

        String erg = tester.sent19();

        // assert statements
        assertEquals("Den leckeren Kuchen zu genießen ist das Beste auf der Welt.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence20() {
        TestMain tester = new TestMain();

        String erg = tester.sent20();

        // assert statements
        assertEquals("Öffentlich zu sprechen liegt Sara.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence21() {
        TestMain tester = new TestMain();

        String erg = tester.sent21();

        // assert statements
        assertEquals("Karl beschließt, der Behörde morgen die Kündigung zu schicken.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence22() {
        TestMain tester = new TestMain();

        String erg = tester.sent22();

        // assert statements
        assertEquals("Annemarie glaubt, nicht zurücktreten zu dürfen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence23() {
        TestMain tester = new TestMain();

        String erg = tester.sent23();

        // assert statements
        assertEquals("Annemarie beschließt, diesen Unsinn nicht länger mitzumachen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence24() {
        TestMain tester = new TestMain();

        String erg = tester.sent24();

        // assert statements
        assertEquals("Steuern hinterzogen zu haben erschreckt Annemarie.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence25() {
        TestMain tester = new TestMain();

        String erg = tester.sent25();

        // assert statements
        assertEquals("Karl wird von Annemarie befohlen, sie mitzunehmen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence26() {
        TestMain tester = new TestMain();

        String erg = tester.sent26();

        // assert statements
        assertEquals("Es scheint zu regnen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence27() {
        TestMain tester = new TestMain();

        String erg = tester.sent27();

        // assert statements
        assertEquals("Karl glaubt, dass Anne zu schlafen wünscht.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence28() {
        TestMain tester = new TestMain();

        String erg = tester.sent28();

        // assert statements
        assertEquals("Oma liebt es, das Kind zu bekochen und zu verhätscheln.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence29() {
        TestMain tester = new TestMain();

        String erg = tester.sent29();

        // assert statements
        assertEquals("Das Kind liebt es, zu naschen oder zu spielen und zu lachen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence30() {
        TestMain tester = new TestMain();

        String erg = tester.sent30();

        // assert statements
        assertEquals("Drei Tore hoffte Karl zu schießen.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence31() {
        TestMain tester = new TestMain();

        String erg = tester.sent31();

        // assert statements
        assertEquals("Maria hat dieses Buch versucht zu verkaufen", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence32() {
        TestMain tester = new TestMain();

        String erg = tester.sent32();

        // assert statements
        assertEquals("Maria hofft, das Leben gelebt und alle Hoffnungen gehofft zu haben.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence33() {
        TestMain tester = new TestMain();

        String erg = tester.sent33();

        // assert statements
        assertEquals("Maria freut sich, diese Bücher lesen zu werden.", erg);
        System.out.println(erg);
    }

    @Test
    public void testSentence34() {
        TestMain tester = new TestMain();

        String erg = tester.sent34();

        // assert statements
        assertEquals("Karl hoffte drei Tore, zwei Enten und ein Foto zu schießen.", erg);
        System.out.println(erg);
    }
}