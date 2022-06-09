package com.CMASProject.SplitReceiptsProject;

import com.CMASProject.SplitReceiptsProject.Enteties.Config;
import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.CMASProject.SplitReceiptsProject.Spring.UploadFile;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        UploadFile uploadFile = new UploadFile();

        String path = System.getenv().get("APPDATA") + "\\SplitProject";
        String configFilePath = path + "\\config.properties";
        File FilePath = new File(configFilePath);
        File Folderpath = new File(path);

        Config config = new Config(Folderpath,FilePath);

        List<Person> list = new ArrayList<>();
        Person Raquel = new Person(214757854,"enq#qm&O+&");
        Raquel.setNodeID("d73d7f3f-0d39-4e16-bb8e-8fd4979e725e");
        FileSystemResource fileraquel = new FileSystemResource(new File(config.getDestinationFolder()+"\\Rv_"+ Raquel.getProcessDate()+" - "+Raquel.getName()+".pdf"));
        Raquel.setFile(fileraquel); // -> Set File for the person
        list.add(Raquel);// -> Raquel Silva Melo Pereira Roldão
        System.out.println(fileraquel.getFilename());
        /*

          -> Para definir os ficheiros devemos fazer um ciclo com a lista de pessoas
            <--TODO--
            List<Person> list = new ArrayList<>();
            for(Person person : list){
            FileSystemResource file = new FileSystemResource(new File(config.getDestinationFolder()+"\\Rv_"+ person.getProcessDate()+" - "+person.getName()+".pdf"));
            list.setFile(file);
            }
            ------->
        */

        Person Susana = new Person(245898190,"dEUt$nWDzh");
        Susana.setNodeID("5e76b17e-0998-4de8-a72e-6305c500cb88");
        FileSystemResource fileSusana = new FileSystemResource(new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\OutPut\\Rv_Abr_2015 - Susana Bessa.pdf"));
        Susana.setFile(fileSusana);
        list.add(Susana);// -> Susana Isabela Lopes Bessa


        Person Vitor = new Person(207293538,"dQWYGndN2K");
        Vitor.setNodeID("f8235f53-4435-44b1-a37e-829a932a6f41");
        FileSystemResource fileVitor = new FileSystemResource(new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\OutPut\\Rv_Abr_2015 - Vítor Monteiro.pdf"));
        Vitor.setFile(fileVitor);
        list.add(Vitor);// -> Vítor Santos Monteiro


        Person Amirhossein = new Person(311290736,"uiriw@yhui");
        Amirhossein.setNodeID("5c73071b-5306-4775-afba-f70ace7fe535");
        FileSystemResource fileAmir = new FileSystemResource(new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\OutPut\\Rv_Mai_2022 - Amirhossein Khameneh.pdf"));
        Amirhossein.setFile(fileAmir);
        list.add(Amirhossein);// -> Amirhossein Khameneh


        Person Fernando = new Person(260639621,"Mylg_F%yEy");
        Fernando.setNodeID("6cb1a92b-dcda-4fe8-8f96-0b8534d5b55a");
        FileSystemResource fileFernando = new FileSystemResource(new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\OutPut\\Rv_Abr_2015 - Fernando Fortunato.pdf"));
        Fernando.setFile(fileFernando);
        list.add(Fernando);// ->  Fernando Miguel dos Santos Fortunato



        String ticket= "TICKET_3f57ff443c9a5d4d850ebe29dce2a72cb4f1007f";

        String Nodeid = "615429e3-6fb1-49da-9fbc-c135fa8ac872";



        uploadFile.fileUpload(list,ticket);
    }
    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
