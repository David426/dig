/*David Sill
CSE 310 Assignment: 2
*/
import org.xbill.DNS.*;

import java.io.IOException;
import java.net.UnknownHostException;


public class MyDig {

    static String[] ROOT_SERVERS = {"198.41.0.4", "199.9.14.201", "192.33.4.12", "199.7.91.13", "192.203.230.10", "192.5.5.241",
    "192.112.36.4", "198.97.190.53", "192.36.148.17", "192.58.128.30", "193.0.14.129", "199.7.83.42", "202.12.27.33"};

    public static void main(String[] args){
        Boolean isResolved = false;
        String queryAddress = args[0];
        Message queryMessage = createQuery(queryAddress);
//        System.out.println(queryMessage.toString());

        try {
            SimpleResolver resolver = new SimpleResolver(ROOT_SERVERS[0]);
            while (!isResolved) {
                Message reply = resolver.send(queryMessage);
                System.out.println(reply.toString());
                Record[] answers = reply.getSectionArray(Section.ANSWER);
                Record[] authorityRecords = reply.getSectionArray(Section.AUTHORITY);
                Record[] additionalRecords = reply.getSectionArray(Section.ADDITIONAL);
//                System.out.println(reply.findRecord(authorityRecords[0], 2));

                if(answers.length > 0){
                    System.out.println("###### Answer Found");
                    if (answers[0].getType() == Type.CNAME) {
                        System.out.printf("CNAME: %s\n", ((CNAMERecord)answers[0]).getTarget().toString());
                        queryAddress = ((CNAMERecord)answers[0]).getTarget().toString();
                        resolver = new SimpleResolver(ROOT_SERVERS[0]);
//                        isResolved=  true;
                    } else {
                        System.out.println("##### QUERY RESOLVED!!#####");
                        System.out.println(reply);
                        System.out.println(answers[0]);
                        isResolved = true;
                    }
                } else if(additionalRecords.length > 0){
                    for (int i = 0; i < additionalRecords.length; i++) {
                        if (additionalRecords[i].getType() == Type.A) {
//                            queryAddress = ((ARecord) additionalRecords[i]).getAddress().getHostAddress();
                            resolver = new SimpleResolver(((ARecord) additionalRecords[i]).getAddress().getHostAddress());
                            break;
                        }
                    }
                } else if (authorityRecords.length > 0) {
//                    queryAddress = authorityRecords[0].getName()
                    System.out.println("#####AUTH RECORD NAME######");
                    queryAddress = ((SOARecord)authorityRecords[0]).getAdmin().toString();
                } else {
                    System.out.println("Something went wrong :/");
                    System.out.println(reply);
                    isResolved = true;
                }
                queryMessage = createQuery(queryAddress);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

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
