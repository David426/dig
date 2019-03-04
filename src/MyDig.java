/*David Sill
CSE 310 Assignment: 2
*/
import org.xbill.DNS.*;

import java.io.IOException;
import java.net.UnknownHostException;


public class MyDig {
    static int ANSWER_SECTION = 1;
    static int AUTHORITY_RECORDS_SECTON = 2;
    static int ADDITIONAL_RECORDS_SECTION = 3;

    static String[] ROOT_SERVERS = {"198.41.0.4", "199.9.14.201", "192.33.4.12", "199.7.91.13", "192.203.230.10", "192.5.5.241",
    "192.112.36.4", "198.97.190.53", "192.36.148.17", "192.58.128.30", "193.0.14.129", "199.7.83.42", "202.12.27.33"};

    public static void main(String[] args){
        Boolean isResolved = false;
        Message queryMessage = createQuery(args[0]);
//        System.out.println(queryMessage.toString());

        try {
            SimpleResolver resolver = new SimpleResolver(ROOT_SERVERS[0]);
            Message reply = resolver.send(queryMessage);
            System.out.println(reply.toString());
            Record[] answers = reply.getSectionArray(ANSWER_SECTION);
            Record[] authorityRecords = reply.getSectionArray(AUTHORITY_RECORDS_SECTON);
            Record[] additionalRecords = reply.getSectionArray(ADDITIONAL_RECORDS_SECTION);
            System.out.println(reply.findRecord(authorityRecords[0], 2));
            for(int i = 0; i < additionalRecords.length; i++){
                System.out.println(additionalRecords[i].getType());
                if(additionalRecords[i].getType() == Type.A){
                    System.out.println(((ARecord)additionalRecords[i]).getAddress());
                }
//                resolver = new SimpleResolver(additionalRecords[0].)
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
//            SimpleResolver resolver = new SimpleResolver("205.251.192.15");
////151.101.210.49
//            Message replyMessage = resolver.send(queryMessage);
//            System.out.println(replyMessage.toString());



        //Our goal
//        try{
//            Lookup look = new Lookup(new Name(args[0]));
//              Lookup look = new Lookup(new Name("www.news12.com"));
//
//            look.run();
//            Record[] records = look.getAnswers();
//            for (Record rec:records) {
//                System.out.println(rec.toString());
//            }
//        }
//        catch (Exception e){
//            System.out.println(e.toString());
//        }

    }

    public static Message createQuery(String query){
        Name queryName = null;
        try {
            if (query.charAt(query.length()-1) != '.'){
                queryName = Name.concatenate(Name.fromString(query), Name.root);
            } else {
                queryName = Name.fromString(query);
            }
        } catch (TextParseException textEx){

        } catch (NameTooLongException nameEx){

        }
//        System.out.println(queryName.toString());
        if(queryName != null){
            Record queryRecord = Record.newRecord(queryName, Type.A, DClass.IN);
            Message queryMessage = Message.newQuery(queryRecord);
            return queryMessage;
        } else {
            return null;
        }
    }
}
