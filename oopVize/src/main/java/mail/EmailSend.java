
/*
 *
 * Muhammed Furkan Kaçmaz
 * 19120205002
 * İstanbul Medeniyet Üniversitesi
 * Bilgisayar Mühendisliği
 * 8.04.2023
 *
 *
 * Bu programda;
 * - kullanicilar dosyasına Elit ve Genel Üye ekleme işlemi yapılabilir.
 * - Sadece Elit üyelere, sadece Genel üyelere veya tüm üyelere smtp server üzerinden toplu mail gönderilebilir.
 *
 * */


package mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.mail.MessagingException;
import java.io.*;
import java.util.Scanner;

@SpringBootApplication
public class EmailSend extends Mail { /*Mail classı miras alınır*/

    @Autowired
    private EmailSenderService senderService;

    @EventListener(ApplicationReadyEvent.class)
    public void triggerMail() {/*Mail gönderme işlemi için burası kullanılacak*/
        senderService.sendSimpleEmail(sentMail, subject, body);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1- Elit üye ekleme\n" + "2- Genel Üye ekleme\n" + "3- Mail Gönderme");
        int sayi = scanner.nextInt();


        /**  ELİT ÜYE EKLEME  */
        if (sayi == 1) {
            int kontrol = 1;/*Bu değişken kullanıcının geçerli mail adresi girip girmediğini kontrol etmek için kullanılır*/

            Person person = new Person();

            System.out.println("Ad: ");
            person.name = scanner.nextLine();
            person.name = scanner.nextLine();
            System.out.println("Soyad: ");
            person.surname = scanner.next();

            /*Eğer kullanıcının girdiği mailde @ ve .com yoksa tekrar girmesi istenecek*/
            while (kontrol == 1) {
                System.out.println("Mail: ");
                person.mail = scanner.next();

                /*Eğer kullanıcının girdiği mailde @ ve .com varsa Elit üye ekleme işelmi yap*/
                if (person.mail.contains("@") && person.mail.contains(".com")) {

                    File okunacakDosya = new File("kullanicilar.txt");
                    File yazilacakDosya = new File("kullanicilar1.txt");

                    BufferedReader dosyaOkuyucu = new BufferedReader(new FileReader(okunacakDosya));
                    FileReader fileReader = new FileReader(okunacakDosya);
                    FileWriter fileWriter = new FileWriter(yazilacakDosya, true);

                    String satir = dosyaOkuyucu.readLine();

                    /*Bu döngüde kullanicilar.txt dosyası sonuna kadar okunup kullanicilar1.txt dosyasına yazılacak*/
                    while (satir != null) {
                        /*Eğer kullanicilar.txt dosyasında boş satıra gelindiyse, üye ekleme işlemi yapılacak(Elit üyelerin altına)*/
                        if (satir.isEmpty()) {
                            fileWriter.write(person.name + "\t" + person.surname + "\t" + person.mail + "\n\n");
                        } else {
                            /*Boş satıra gelinmediyse kullanicilar.txt den satır okunup kullanicilar1.txt ye yazılacak*/
                            fileWriter.write(satir + "\n");
                        }
                        /*Döngü her döndüğünde 1 satır okunur*/
                        satir = dosyaOkuyucu.readLine();
                    }

                    /*Açılan dosyalar kapatılır*/
                    fileReader.close();
                    fileWriter.close();
                    dosyaOkuyucu.close();


                    okunacakDosya.delete();/*kullanicilar.txt silinir*/
                    yazilacakDosya.renameTo(new File("kullanicilar.txt"));/*kullanicilar1.txt adi kullanicilar.txt şeklinde güncellenir*/

                    System.out.println("Elit Üye ekleme işlemi tamamlandı.");

                    kontrol = 0;

                } else {
                    System.out.println("Geçersiz mail");
                    kontrol = 1;
                }
            }

            /**  GENEL ÜYE EKLEME  */
        } else if (sayi == 2) {


            int kontrol = 1;/*Bu değişken kullanıcının geçerli mail adresi girip girmediğini kontrol etmek için kullanılır*/

            Person person = new Person();

            System.out.println("Ad: ");
            person.name = scanner.nextLine();
            person.name = scanner.nextLine();
            System.out.println("Soyad: ");
            person.surname = scanner.next();

            /*Eğer kullanıcının girdiği mailde @ ve .com yoksa tekrar girmesi istenecek*/
            while (kontrol == 1) {
                System.out.println("Mail: ");
                person.mail = scanner.next();

                /*Eğer kullanıcının girdiği mailde @ ve .com varsa Genel üye ekleme işelmi yap*/
                if (person.mail.contains("@") && person.mail.contains(".com")) {
                    try {
                        File dosya = new File("kullanicilar.txt");
                        FileWriter fileWriter = new FileWriter(dosya, true);

                        /*FileWriter ile dosyanın sonuna aralarında tab olacak şekilde Genel üye bilgileri yazılır*/
                        fileWriter.write(person.name + "\t" + person.surname + "\t" + person.mail + "\n");

                        fileWriter.close();

                        System.out.println("Genel Üye ekleme işlemi tamamlandı.");

                    } catch (IOException e) {
                        System.out.println("Dosya ekleme hatası: " + e.getMessage());
                    }
                    kontrol = 0;

                } else {
                    System.out.println("Geçersiz mail");
                    kontrol = 1;
                }
            }


            /**  MAİL GÖNDERME  */
        } else if (sayi == 3) {
            System.out.println("1- Elit üyelere mail\n" + "2- Genel üyelere mail\n" + "3- Tüm üyelere mail");
            int mailMenu = scanner.nextInt();

            /**  Elit Üyelere Mail Gönderme  */
            if (mailMenu == 1) {

                /*Mail Bilgileri alınır*/
                Mail mail = new Mail();
                System.out.println("Mail Başlığı: ");
                mail.mailSubject = scanner.nextLine();
                mail.mailSubject = scanner.nextLine();
                System.out.println("Mail Açıklaması: ");
                mail.mailBody = scanner.nextLine();

                File okunacakDosya = new File("kullanicilar.txt");
                BufferedReader dosyaOkuyucu = new BufferedReader(new FileReader(okunacakDosya));

                String satir = dosyaOkuyucu.readLine();
                String cumle = "";/*Satır parçalama işleminde kullanılır*/

                /*kullanicilar.txt dosyasında boş satıra kadar olan tüm satırları okur*/
                while (!satir.isEmpty()) {
                    /*Okunan satırlarda @ ve .com varsa parçalama işlemi yapılır*/
                    if (satir.contains("@") && satir.contains(".com")) {
                        cumle = satir;
                        String[] kelimeler = cumle.split(" ");
                        int sonEleman = kelimeler.length - 1;

                        mail.mail = kelimeler[sonEleman];

                        /*Oluşturulan Mail nesnesine mail bilgileri atanır*/
                        setMail(mail.mail);
                        setSubject(mail.mailSubject);
                        setBody(mail.mailBody);

                        /*Mail gönderilir*/
                        SpringApplication.run(EmailSend.class, args);
                    }
                    satir = dosyaOkuyucu.readLine();
                }

                dosyaOkuyucu.close();

                /**  Genel Üyelere Mail Gönderme  */
            } else if (mailMenu == 2) {

                /*Mail Bilgileri alınır*/
                Mail mail = new Mail();
                System.out.println("Mail Başlığı: ");
                mail.mailSubject = scanner.nextLine();
                mail.mailSubject = scanner.nextLine();
                System.out.println("Mail Açıklaması: ");
                mail.mailBody = scanner.nextLine();

                File okunacakDosya = new File("kullanicilar.txt");
                BufferedReader dosyaOkuyucu = new BufferedReader(new FileReader(okunacakDosya));

                String satir = dosyaOkuyucu.readLine();
                String cumle = "";/*Satır parçalama işleminde kullanılır*/
                int sayac = 0;/*kullanicilar.txt dosyasında okuma yapılırken boş satırdan sonraki mailleri tespit etmek için kullanılır*/

                /*Dosyasın sonuna kadar okuma işlemi yapılır*/
                while (satir != null) {

                    /*Eğer sayac 1'se bundan sonraki satırlarda genel üyeler vardır*/
                    if (sayac == 1) {

                        /*Her satır boşluk karakteriyle parçalanarak elde edilen maile mail gönderme işlemi yapılır*/
                        if (satir.contains("@") && satir.contains(".com")) {
                            cumle = satir;
                            String[] kelimeler = cumle.split(" ");
                            int sonEleman = kelimeler.length - 1;

                            mail.mail = kelimeler[sonEleman];

                            setMail(mail.mail);
                            setSubject(mail.mailSubject);
                            setBody(mail.mailBody);

                            SpringApplication.run(EmailSend.class, args);
                        }
                    }

                    /*Eğer boş satır okunduysa(Elit üyeler geçildiyse) sayac değişkenine 1 atanır*/
                    if (satir.isEmpty()) {
                        sayac = 1;
                    }

                    satir = dosyaOkuyucu.readLine();
                }

                dosyaOkuyucu.close();


                /**  Tüm Üyelere Mail Gönderme  */
            } else if (mailMenu == 3) {

                /*Mail Bilgileri alınır*/
                Mail mail = new Mail();
                System.out.println("Mail Başlığı: ");
                mail.mailSubject = scanner.nextLine();
                mail.mailSubject = scanner.nextLine();
                System.out.println("Mail Açıklaması: ");
                mail.mailBody = scanner.nextLine();

                File okunacakDosya = new File("kullanicilar.txt");
                BufferedReader dosyaOkuyucu = new BufferedReader(new FileReader(okunacakDosya));

                String satir = dosyaOkuyucu.readLine();
                String cumle = "";

                /*Dosyanın sonuna kadar tüm satırlardaki maillere mail gönderilme işlemi yapılır*/
                while (satir != null) {
                    if (satir.contains("@") && satir.contains(".com")) {
                        cumle = satir;
                        String[] kelimeler = cumle.split(" ");
                        int sonEleman = kelimeler.length - 1;

                        mail.mail = kelimeler[sonEleman];

                        setMail(mail.mail);
                        setSubject(mail.mailSubject);
                        setBody(mail.mailBody);

                        /*Spring başlatılır*/
                        SpringApplication.run(EmailSend.class, args);
                    }

                    satir = dosyaOkuyucu.readLine();
                }

                dosyaOkuyucu.close();


            } else {
                System.out.println("Geçersiz Cevap !");
            }

        } else {
            System.out.println("Geçersiz Cevap !");
        }


    }

}

class Person {
    public String name;
    public String surname;
    public String mail;
}

class Mail {
    public String mail;
    public String mailSubject;
    public String mailBody;

    protected static String sentMail = "mahoo3447@gmail.com";
    protected static String subject = "";
    protected static String body = "";

    public static void setMail(String _mail) {
        sentMail = _mail;
    }

    public static void setSubject(String _subject) {
        subject = _subject;
    }

    public static void setBody(String _body) {
        body = _body;
    }
}



