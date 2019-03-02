/*David Sill
CSE 310 Assignment: 2
*/
import org.xbill.DNS.*;


public class MyDig {

    public static void main(String[] args){
        try {
            Name queryName = Name.concatenate(Name.fromString(args[0]), Name.root);
            Record queryRecord = Record.newRecord(queryName, Type.A, DClass.ANY);
            Message queryMessage = Message.newQuery(queryRecord);
            System.out.println(queryMessage.toString());

            SimpleResolver resolver = new SimpleResolver("198.41.0.4");
            Message replyMessage = resolver.send(queryMessage);
            System.out.println(replyMessage.toString());
        } catch (TextParseException textEx){
            System.err.println("Error: Unable to parse domain name");
        } catch (Exception e) {
            System.err.println(e.toString());
        }


        //Our goal
//        try{
//            Lookup look = new Lookup(new Name(args[0]));
//            look.run();
//            Record[] records = look.getAnswers();
//            for (Record rec:records) {
//                System.out.println(rec.toString());
//            }
//        }
//        catch (Exception e){
//            System.out.println(e.toString());
//        }
//
    }
}
