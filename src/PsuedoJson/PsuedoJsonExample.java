package PsuedoJson;

import static java.lang.System.out;

public class PsuedoJsonExample {
    public static void main(String[] args) {
        PsuedoJsonObject pjo1 = new PsuedoJsonObject("PJO1");
        pjo1.addPair("Key1", "Value1");
        
        PsuedoJsonObject pjo2 = new PsuedoJsonObject("PJO2");
        
        out.println("Cloning...");
        pjo2.addPair("clone", pjo2);
        
        out.println("Subobject...");
        pjo2.addPair("Obj1", pjo1);
        
        out.println("Create infinite loop...");
        pjo1.addPair("Bad Idea", pjo2);
        
        pjo1.displayData();
        pjo2.displayData();
    }
}
