package simplenlg.realiser;

import simplenlg.features.*;
import simplenlg.lexicon.lexicalitems.Conjunction;
import simplenlg.lexicon.lexicalitems.Verb;

/**
 * This class represents Phrases with Infinitiv+zu.
 *
 * <P>
 * Typically, a <code>INFPhraseSpec</code> consists of:
 * <UL>
 * <LI>the head, a {@link simplenlg.lexicon.lexicalitems.Verb}</LI>
 * (zero) or one comma,
 * zero or (one) conjunction (um, anstatt, ohne),
 * zero or (one) Object-Phrase (which can hold multiple objects-phrases as well),
 * zero or (one) negator
 * zero or (one) Hilfsverb
 * the Verb-Prefix "zu"
 *
 * default is (unmarked)
 *
 * it can be coordinated with other INFPhraseSpecs.
 *
 * for further documentation see "Modifikation von SimpleNLG für ein Generierungssystem" by Felix Theodor
 *
 *
 * created by Felix Theodor
 */

public class INFPhraseSpec extends HeadedPhraseSpec<Verb> {

    @Override
    public <T extends Phrase> Phrase coordinate(T... specs) {
        return null;
    }

    //---------------------------------Creation--------------------------------------------------------------//

    public INFPhraseSpec (){
        super();
        this.category = Category.VERB;
        this.negated = false;
    }


    public INFPhraseSpec(String v, boolean comma) {
        this();
        setHead(v);
        createPreMods(comma);
    }

    private void createPreMods(boolean comma) {
        //comma
        doComma(comma);
        //specialTypes
        this.addPremodifier(1, "");
        //objects
        this.addPremodifier(2,"");
        //nicht
        doNegation();
        //Tense
        this.addPremodifier(4,"");
        //zu
        if (this.head.getVerbPrefix()!= "") {
            this.head.setVerbPrefix(this.head.getVerbPrefix() + "zu");
        }
        else{
            this.addPremodifier(5, "zu");
        }
    }



    //---------------------------------HeadVerb--------------------------------------------------------------//

    @Override
    public void setHead(String v) {
        Verb verb = castToVerb(v);
        this.head = verb;
    }

    Verb castToVerb(String verb) {
        Verb v = new Verb(verb);
        return v;
    }



    //---------------------------------Conjunctions--------------------------------------------------------------//

    public void setFinal(){
        this.addPremodifier(1, "um");
    }

    public void setContrast(){ this.addPremodifier(1, "anstatt"); }

    public void setKonzessiv() {
        this.addPremodifier(1, "ohne");
    }




    //---------------------------------Negation--------------------------------------------------------------//

    @Override
    public void setNegated(boolean neg) {
        this.negated = neg;
        doNegation();
    }

    public void doNegation(){
        if (negated)
            this.addPremodifier(3, "nicht");
        else
            this.addPremodifier(3,"");
    }


    //---------------------------------Comma--------------------------------------------------------------//

    public void doComma(boolean comma){
        if (comma)
            this.addPremodifier(0, ",");
        else {
            if (!premodifiers.isEmpty())
                premodifiers.remove(0);
            this.addPremodifier(0, "");
        }
    }


    //---------------------------------Objects--------------------------------------------------------------//

    public void setObject(NPPhraseSpec np) {
        premodifiers.remove(2);
        np.setCase(Case.ACCUSATIVE);
        this.addPremodifier(2, np );
    }

    public void setObject(NPPhraseSpec np, Case specificCase) {
        premodifiers.remove(2);
        np.setCase(specificCase);
        this.addPremodifier(2, np );
    }

    public void setObject(Phrase ap) {
        premodifiers.remove(2);
        this.addPremodifier(2, ap );
    }

    public void setObject (String cannedText){
        premodifiers.remove(2);
        this.addPremodifier(2, cannedText);
    }

    public void setObject (String cannedText, boolean negated){
        premodifiers.remove(2);
        if (negated)
            cannedText = "nicht " + cannedText;
        this.addPremodifier(2, cannedText);
    }


    public NPPhraseSpec getObject(){
        NPPhraseSpec obj = (NPPhraseSpec) premodifiers.get(2);
        return obj;
    }



    //---------------------------------Coordination--------------------------------------------------------------//

    public void coordinate(INFPhraseSpec ip){
        this.addPostmodifier("und");
        ip.doComma(false);
        if (this.head.getBaseForm().equals(ip.head.getBaseForm()))
            supressVerbRealisation();
        this.addPostmodifier(ip);
    }

    public void coordinate(INFPhraseSpec ip, String conj){
        this.addPostmodifier(conj);
        ip.doComma(false);
        if (this.head.getBaseForm().equals(ip.head.getBaseForm()))
            supressVerbRealisation();
        this.addPostmodifier(ip);
    }

    public void coordinate(INFPhraseSpec ip, Conjunction conj){
        this.addPostmodifier(conj);
        ip.doComma(false);
        if (this.head.getBaseForm().equals(ip.head.getBaseForm()))
            supressVerbRealisation();
        this.addPostmodifier(ip);
    }

    //double coordination

    public void coordinate(INFPhraseSpec ip, INFPhraseSpec ip2){
        this.addPostmodifier(",");
        ip.addPostmodifier("und");

        ip.doComma(false);
        ip2.doComma(false);
        if (this.head.getBaseForm().equals(ip.head.getBaseForm()) && this.head.getBaseForm().equals(ip2.head.getBaseForm())) {
            supressVerbRealisation();
            ip.supressVerbRealisation();
        }
        this.addPostmodifier(ip);
        this.addPostmodifier(ip2);
    }

    public void coordinate(INFPhraseSpec ip, INFPhraseSpec ip2, String conj){
        this.addPostmodifier(",");
        ip.addPostmodifier(conj);

        ip.doComma(false);
        ip2.doComma(false);
        if (this.head.getBaseForm().equals(ip.head.getBaseForm()) && this.head.getBaseForm().equals(ip2.head.getBaseForm())) {
            supressVerbRealisation();
            ip.supressVerbRealisation();
        }
        this.addPostmodifier(ip);
        this.addPostmodifier(ip2);
    }

    public void supressVerbRealisation(){
        setHead("");
        if (this.premodifiers.get(5) != null) {
            this.premodifiers.remove(5);
            addPremodifier(5,"");
        }
    }

    //----------------------------------------------TENSE--------------------------------------------------//

    public void setTense(Tense tense){
        if (tense.equals(Tense.PAST)){
            premodifiers.remove(4);
            addPremodifier(4, head.getPerfectParticiple());
            setAux(tense);
        }
        else if (tense.equals(Tense.FUTURE)){
            premodifiers.remove(4);
            addPremodifier(4, head.getInfinitive());
            setAux(tense);
        }
    }

    public void setAux (Tense tense){
        if (tense.equals(Tense.PAST)) {
            if (head.getAuxType().equals(AuxType.HABEN))
                setHead("haben");
            else
                setHead("sein");
        }
        else{
            setHead("werden");
        }
    }
}
