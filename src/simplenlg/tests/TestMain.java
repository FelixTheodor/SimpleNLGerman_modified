package simplenlg.tests;

import simplenlg.features.*;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.DBLexicon;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.db.XMLAccessor;
import simplenlg.lexicon.lexicalitems.Verb;
import simplenlg.realiser.*;
import simplenlg.realiser.comparators.WordOrder;


public class TestMain {

    //change directory to your folder, otherwise the tests will fail
    public static Lexicon lex = new DBLexicon(new XMLAccessor("C:\\Users\\felix\\OneDrive\\Desktop\\SimpleNLGerman_modified\\res\\toy-lexicon.xml"));
    public static Realiser r = new Realiser(lex);
    public static NLGFactory factory = new NLGFactory(lex);



    public static String sent1() {
        NPPhraseSpec er = factory.createNounPhrase("Er");
        NPPhraseSpec book = factory.createNounPhrase("das Buch");
        INFPhraseSpec understand = factory.createInfPhrase("verstehen", true);
        understand.setNegated(true);
        understand.setObject(book);
        Verb v = factory.createVerb("glauben");
        SPhraseSpec s3 = factory.createSentence(er, v);
        //s3.setPassive(true);
        s3.addComplement(understand);
        //s3.setNegated(true);


        String real2 = r.realise(s3);
        return(real2);
    }



    public static  String sent2()
    {
        NPPhraseSpec np = factory.createNounPhrase("der Mann");
        INFPhraseSpec ip = factory.createInfPhrase("hören");
        ip.setObject(np);
        ip.setNegated(true);
        SPhraseSpec s2 = factory.createSentence("die Frau", "versuchen", ip);

        //s2.addComplement(ip);

        String real = r.realise(s2);
        return(real);
    }

    public static  String sent3(){
        NPPhraseSpec np = factory.createNounPhrase("ich");
        NPPhraseSpec london = factory.createNounPhrase("London");
        london.addPremodifier("nach");
        INFPhraseSpec ip = factory.createInfPhrase("fahren");
        ip.setObject(london);

        SPhraseSpec s = factory.createSentence(np, "überlegen", ip);

        return (r.realise(s));
    }

    public static  String sent4(){
        NPPhraseSpec np = factory.createNounPhrase("ich");
        NPPhraseSpec pro=factory.createNounPhrase("Anna");
        INFPhraseSpec ip = factory.createInfPhrase("bleiben");

        SPhraseSpec s = factory.createSentence(np, "empfehlen", pro);
        s.addModifier(ip);

        return (r.realise(s));
    }

    public static  String sent5(){
        NPPhraseSpec np = factory.createNounPhrase("Karl");
        NPPhraseSpec pro=factory.createPronounPhrase("Anna");
        pro.setCase(Case.DATIVE);
        NPPhraseSpec poss = factory.createNounPhrase("die","Möglichkeit");
        INFPhraseSpec ip = factory.createInfPhrase("sterben");
        ip.setNegated(true);

        SPhraseSpec s = factory.createSentence(np,"schenken",pro);
        s.addModifier(poss);
        s.addModifier(ip);

        return (r.realise(s));
    }

    public static  String sent6(){
        NPPhraseSpec np = factory.createPronounPhrase("er");
        np.setGender(Gender.FEMININE);
        np.setCase(Case.DATIVE);
        NPPhraseSpec pro=factory.createPersonalPronounPhrase(Person.SECOND, NumberAgr.SINGULAR);
        INFPhraseSpec ip = factory.createInfPhrase("auf|hören", false);

        SPhraseSpec s = factory.createSentence(ip, "schwer|fallen");
        s.addModifier(np);
        s.setNegated(true);

        return (r.realise(s));
    }

    public static  String sent7(){
        NPPhraseSpec poss = factory.createNounPhrase("die","Möglichkeit");
        INFPhraseSpec ip = factory.createInfPhrase("sterben");
        ip.setNegated(true);
        poss.addModifier(ip);

        SPhraseSpec s = factory.createSentence(poss,"sein");
        s.addModifier("fantastisch");


        return (r.realise(s));
    }

    public static  String sent8(){
        NPPhraseSpec np = factory.createNounPhrase("der Mann");
        INFPhraseSpec ip = factory.createInfPhrase("schweben");

        SPhraseSpec s = factory.createSentence(np, "glauben", ip);

        return (r.realise(s));
    }

    public static  String sent9(){
        NPPhraseSpec np = factory.createNounPhrase("die Frau");
        INFPhraseSpec ip = factory.createInfPhrase("verlieren");
        NPPhraseSpec np2 = factory.createNounPhrase("dieses Mal");
        ip.setNegated(true);
        //np2.setNegated(true);

        ip.setObject(np2);


        SPhraseSpec s = factory.createSentence(np, "hoffen", ip);

        return (r.realise(s));
    }

    public static  String sent10(){
        INFPhraseSpec ip = factory.createInfPhrase("zu|hören");
        AdjPhraseSpec a = factory.createAdjectivePhrase("wichtig");

        SPhraseSpec s = factory.createSentence("sein");
        s.addModifier(a);
        s.addModifier(ip);

        return (r.realise(s));
    }

    public static  String sent11(){
        NPPhraseSpec du = factory.createPronounPhrase("du");
        INFPhraseSpec ip = factory.createInfPhrase("folgen", false);


        SPhraseSpec s = factory.createSentence(du, "haben");
        s.addModifier(ip);

        return (r.realise(s));
    }

    public static  String sent12(){
        NPPhraseSpec du = factory.createPronounPhrase("du");
        NPPhraseSpec dich = factory.createPronounPhrase("du");
        dich.setCase(Case.ACCUSATIVE);
        INFPhraseSpec ip = factory.createInfPhrase("unterwerfen", false);


        SPhraseSpec s = factory.createSentence(du, "haben", dich);
        s.addModifier(ip);

        return (r.realise(s));
    }

    public static  String sent13(){
        NPPhraseSpec du = factory.createPronounPhrase("er");
        INFPhraseSpec ip = factory.createInfPhrase("helfen");
        ip.setFinal();


        SPhraseSpec s = factory.createSentence(du, "kommen");
        s.addModifier(ip);
        s.setTense(Tense.PAST);

        return (r.realise(s));
    }

    public static  String sent14(){
        NPPhraseSpec du = factory.createPronounPhrase("er");
        NPPhraseSpec sich = factory.createReferentialNounPhrase(du, "sich");
        INFPhraseSpec ip = factory.createInfPhrase("regen");
        ip.setKonzessiv();
        ip.setObject(sich);


        SPhraseSpec s = factory.createSentence(du, "da|bleiben");
        s.addModifier(ip);
        s.setTense(Tense.PAST);

        return (r.realise(s));
    }

    public static  String sent15(){
        NPPhraseSpec du = factory.createPronounPhrase("er");
        INFPhraseSpec ip = factory.createInfPhrase("blinzeln");
        ip.setKonzessiv();


        SPhraseSpec s = factory.createSentence(du, "da|bleiben");
        s.addSubordinate(ip);
        s.setTense(Tense.PAST);

        return (r.realise(s));
    }

    public static  String sent16(){
        NPPhraseSpec du = factory.createPronounPhrase("er");
        INFPhraseSpec ip = factory.createInfPhrase("helfen");
        ip.setContrast();


        SPhraseSpec s = factory.createSentence(du, "lesen");
        s.addModifier(Position.DEFAULT, ip);
        s.setTense(Tense.PAST);

        return (r.realise(s));
    }

    public static  String sent17(){
        SPhraseSpec s=factory.createSentence ("die Frau","wünschen");
        INFPhraseSpec ip = factory.createInfPhrase("an|kommen");
        ip.setObject("am Hotel");

        s.addSubordinate(ip);

        return (r.realise(s));
    }

    public static  String sent18(){
        INFPhraseSpec ip = factory.createInfPhrase("der Ball","flach|halten", false);
        SPhraseSpec s=factory.createSentence (ip, "sein");
        s.addModifier("das Wichtigste");

        return (r.realise(s));
    }

    public static  String sent19(){
        INFPhraseSpec ip = factory.createInfPhrase("genießen", false);
        NPPhraseSpec ob = factory.createNounPhrase("der Kuchen");
        ob.addPremodifier("leckeren");
        ip.setObject(ob);
        SPhraseSpec s=factory.createSentence (ip, "sein");
        NPPhraseSpec mod = factory.createNounPhrase("das Beste");
        mod.addModifier("auf der Welt");
        s.addModifier(mod);

        return (r.realise(s));
    }

    public static  String sent20(){
        INFPhraseSpec ip = factory.createInfPhrase("sprechen", false);
        NPPhraseSpec ob = factory.createPronounPhrase("Sara");
        AdjPhraseSpec ap = factory.createAdjectivePhrase("öffentlich");
        ip.setObject(ap);
        SPhraseSpec s=factory.createSentence (ip, "liegen", ob);


        return (r.realise(s));
    }

    public static  String sent21(){
        INFPhraseSpec ip = factory.createInfPhrase("schicken", true);
        NPPhraseSpec ob = factory.createNounPhrase("die Kündigung");
        NPPhraseSpec be = factory.createNounPhrase("die Behörde");
        be.addPostmodifier("morgen");
        be.addPostmodifier(ob);
        ip.setObject(be, Case.DATIVE);
        SPhraseSpec s=factory.createSentence ("Karl", "beschließen", ip);


        return (r.realise(s));
    }

    public static  String sent22(){
        INFPhraseSpec ip = factory.createInfPhrase("dürfen", true);
        //ip.setNegated(true);
        ip.setObject("zurücktreten", true);
        NPPhraseSpec ob = factory.createNounPhrase("Annemarie");
        SPhraseSpec s=factory.createSentence (ob, "glauben", ip);


        return (r.realise(s));
    }

    public static  String sent23(){
        INFPhraseSpec ip = factory.createInfPhrase("mit|machen", true);
        NPPhraseSpec unsinn = factory.createNounPhrase("diesen Unsinn");
        AdjPhraseSpec lang = factory.createAdjectivePhrase("lang");
        lang.setDegree(AdjectiveDegree.COMPARATIVE);
        lang.setNegated(true);
        unsinn.setPostmodifier(lang);
        ip.setObject(unsinn);
        NPPhraseSpec ob = factory.createNounPhrase("Annemarie");
        SPhraseSpec s=factory.createSentence (ob, "beschließen", ip);


        return (r.realise(s));
    }

    public static  String sent24(){
        INFPhraseSpec ip = factory.createInfPhrase("hinterziehen", false);
        NPPhraseSpec ips = factory.createNounPhrase("Steuern");
        ip.setObject(ips);
        ip.setTense(Tense.PAST);
        SPhraseSpec s = factory.createSentence(ip,"erschrecken", "Annemarie");



        return (r.realise(s));
    }

    public static  String sent25(){
        INFPhraseSpec ip = factory.createInfPhrase("mit|nehmen", true);
        NPPhraseSpec ob = factory.createNounPhrase("Annemarie");
        ob.setGender(Gender.FEMININE);
        NPPhraseSpec ips = factory.createReferentialNounPhrase(ob);
        ip.setObject(ips);
        SPhraseSpec s=factory.createSentence (ob, "befehlen", "Karl");
        s.setPassive(true);
        s.addSubordinate(ip);

        return (r.realise(s));
    }

    public static  String sent26(){
        INFPhraseSpec ip = factory.createInfPhrase("regnen", false);
        SPhraseSpec s=factory.createSentence ("scheinen");
        s.addModifier(ip);

        return (r.realise(s));
    }

    public static  String sent27(){
        INFPhraseSpec ip = factory.createInfPhrase("schlafen", false);
        SPhraseSpec s=factory.createSentence ("Anne","wünschen");
        SPhraseSpec s2 = factory.createSentence("Karl","glauben");
        s.addModifier(ip);
        s2.addSubordinate("dass",s);

        return (r.realise(s2));
    }

    public static  String sent28(){
        INFPhraseSpec ip = factory.createInfPhrase("das Kind", "bekochen", true);
        INFPhraseSpec ip2 = factory.createInfPhrase("verhätscheln");
        SPhraseSpec s=factory.createSentence ("Oma","lieben", "es");
        ip.coordinate(ip2);
        s.addSubordinate(ip);

        return (r.realise(s));
    }

    public static  String sent29(){
        INFPhraseSpec ip = factory.createInfPhrase("spielen", true);
        INFPhraseSpec ip2 = factory.createInfPhrase("naschen");
        INFPhraseSpec ip3 = factory.createInfPhrase("lachen");
        SPhraseSpec s=factory.createSentence ("Das Kind","lieben", "es");
        ip.coordinate(ip3);
        ip2.coordinate(ip, "oder");
        s.addSubordinate(ip2);

        return (r.realise(s));
    }

    public static  String sent30(){
        INFPhraseSpec ip = factory.createInfPhrase("schießen", false);
        NPPhraseSpec karl = factory.createNounPhrase("Karl");
        karl.setGender(Gender.MASCULINE);
        NPPhraseSpec tor = factory.createNounPhrase("Tor");
        tor.setPlural(true);
        tor.setPremodifier("drei");
        SPhraseSpec s=factory.createSentence (karl,"hoffen", tor);

        s.addModifier(ip);
        s.setTense(Tense.PAST);
        s.setWordOrder(WordOrder.OIS);


        return (r.realise(s));
    }

    public static  String sent31(){
        INFPhraseSpec ip = factory.createInfPhrase("verkaufen", false);
        NPPhraseSpec buch = factory.createNounPhrase("dieses Buch");


        NPPhraseSpec maria = factory.createNounPhrase("Maria");

        SPhraseSpec s=factory.createSentence (maria,"versuchen", buch );

        s.setPerfect(true);

        s.addSubordinate(ip);


        return (r.realise(s));
    }

    public static  String sent32(){
        INFPhraseSpec ip = factory.createInfPhrase("hoffen", false);
        INFPhraseSpec ip2 = factory.createInfPhrase("leben", false);

        ip2.setObject("das Leben");
        ip2.setTense(Tense.PAST);
        ip.setObject("alle Hoffnungen");
        ip.setTense(Tense.PAST);

        ip2.coordinate(ip);


        NPPhraseSpec maria = factory.createNounPhrase("Maria");

        SPhraseSpec s=factory.createSentence (maria,"hoffen");

        s.addSubordinate(ip2);


        return (r.realise(s));
    }

    public static  String sent33(){
        INFPhraseSpec ip = factory.createInfPhrase("lesen");


        ip.setObject("diese Bücher");
        ip.setTense(Tense.FUTURE);


        NPPhraseSpec maria = factory.createNounPhrase("Maria");
        NPPhraseSpec sich = factory.createReferentialNounPhrase(maria, "sich");

        SPhraseSpec s=factory.createSentence (maria,"freuen", sich );

        s.addModifier(ip);


        return (r.realise(s));
    }

    public static  String sent34(){
        INFPhraseSpec ip = factory.createInfPhrase("schießen", false);
        NPPhraseSpec karl = factory.createNounPhrase("Karl");
        karl.setGender(Gender.MASCULINE);
        NPPhraseSpec tor = factory.createNounPhrase("Tor");
        tor.setPlural(true);
        tor.setPremodifier("drei");
        ip.setObject(tor);


        INFPhraseSpec ip2 = factory.createInfPhrase("schießen", false);
        NPPhraseSpec ente = factory.createNounPhrase("Enten");
        ente.setPremodifier("zwei");
        ip2.setObject(ente);

        INFPhraseSpec ip3 = factory.createInfPhrase("schießen", false);
        NPPhraseSpec foto = factory.createNounPhrase("Foto");
        foto.setPremodifier("ein");
        ip3.setObject(foto);

        ip.coordinate(ip2, ip3);

        SPhraseSpec s=factory.createSentence (karl,"hoffen");


        s.addModifier(ip);
        s.setTense(Tense.PAST);
        s.setWordOrder(WordOrder.OIS);


        return (r.realise(s));

    }
}
