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

        } catch (TextParseException textEx){
            System.err.println("Error: Unable to parse domain name");
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        
        //Our goal
        try{
            Lookup look = new Lookup(new Name(args[0]));
            look.run();
            Record[] records = look.getAnswers();
            for (Record rec:records) {
                System.out.println(rec.toString());
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    private Message messageWithNodes(String query, String[] names) throws TextParseException {
        Name queryName = Name.fromString(query);
        Record question = Record.newRecord(queryName, Type.SRV, DClass.IN);
        Message queryMessage = Message.newQuery(question);
        Message result = new Message();
        result.setHeader(queryMessage.getHeader());
        result.addRecord(question, Section.QUESTION);

        for (String name1 : names){
            result.addRecord(new SRVRecord(queryName, DClass.IN, 1, 1, 1, 8080, Name.fromString(name1)), Section.ANSWER);
        }

        return result;
    }
}
