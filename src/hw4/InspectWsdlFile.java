package hw4;

import com.ibm.wsdl.OperationImpl;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

public class InspectWsdlFile {

    public static void main(String[] args) throws Exception {

        WsdlParser p1 = new WsdlParser("drink3.wsdl");

        WsdlParser p2 = new WsdlParser("wine3.wsdl");

        List<OperationImpl> arropout = p1.getOperations();

        //     for (OperationImpl op : arropout) {
        //         System.out.println(op.getName());
        //}
        List<OperationImpl> arropin = p2.getOperations();
        //for (OperationImpl op : arropin) {
        // System.out.println(op.getName());
        //}




        QName qsave = null;
        for (OperationImpl opout : arropout) {

            System.out.println("    OPERATION:  " + opout.getName());

            ArrayList<QName> outElements = p1.getOutputElements(opout);

            for (QName q : outElements) {
//                System.out.println("out: "+  q.getLocalPart());
            }

            for (OperationImpl opin : arropin) {
                ArrayList<QName> inputElements = p2.getInputElements(opin);


                for (QName q1 : outElements) {

                    ArrayList<String> elementsMatchingQnamesOUT = p1.getElementsMatchingQnames(q1);

                    for (QName q2 : inputElements) {

                        System.out.println("    OPOUT: " + opout.getName() + " OPin " + opin.getName());
                        q2.equals(q2);
                        //System.out.println("comparing " + q1.getLocalPart() + " and " + q2.getLocalPart() + " \n");


                        ArrayList<String> elementsMatchingQnamesIN = p2.getElementsMatchingQnames(q2);
                        //System.out.println("opin: " + opin.getName() + " opout: " + opout.getName());
                        //System.out.println("qname in: " + q2.getLocalPart());
                        cmpArrays(elementsMatchingQnamesIN, elementsMatchingQnamesOUT);
                        qsave = q2;
                    }
                }


            }
        }

        /*System.out.println("qsave: " + qsave.getLocalPart()+"\n");
         ArrayList<String> elementsMatchingQnames = p1.getElementsMatchingQnames(qsave);
         for(String s : elementsMatchingQnames){
         System.out.println(s);
         }*/



    }

    private static void cmpArrays(ArrayList<String> s1, ArrayList<String> s2) {
        for (String a : s1) {
             //System.out.println("AAAAAAAAAAAAA" +a);
        }

        //System.out.println("ENDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");


        for (String a : s1) {
            for (String b : s2) {
                double similarity = EditDistance.getSimilarity(a, b);
                if(similarity>0.8)
                System.out.println("a: " + a + " b: " + b + " " + similarity);
                // System.out.println("ENDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                 SimilarityAssessor sim = new SimilarityAssessor();
                 String word1 = a;
                 String word2 = b;
                 // you can choose the proper metric among the implemented one by specifying its name.
                 String metric = SimilarityAssessor.PIRRO_SECO_METRIC;
                 double score;
                 try {
                 score = sim.getSimilarity(word1, word2, metric);
                 if(score>0.85)
                     System.out.println("Semantic Similarity between " + word1 + " and " + word2 + " score " + score + " using " + metric + " metric");
                 } catch (WordNotFoundException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
                 }
            }
        }
    }
}