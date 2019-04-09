package com.example.quizapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    //public String webURL = "https://dota2.gamepedia.com/Heroes";
    public String webURL = "https://www.giantbomb.com/crash-bandicoot/3025-116/characters/";
    //public String webURL = "https://listas.20minutos.es/lista/heroes-de-dota-314130/";
    private ArrayList<String> gameCharacters;
    private ArrayList<String> imageURLS;
    private int correctAnswerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameCharacters = new ArrayList<>();
        this.correctAnswerIndex = 0;

        DownloadTest test = new DownloadTest();
        String result = "";
        try {
            result = test.execute(this.webURL).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Hola " + result);
        Log.i("Hola", result);
        // Characters
        Document document = Jsoup.parse(result);
        Elements characters = document.getElementsByTag("h3");

        for (Element paragraph : characters) {
            this.gameCharacters.add(paragraph.text());
            Log.i("Ches", paragraph.text());
        }

        // Se eliminan los ultimos 3
        this.gameCharacters.remove(this.gameCharacters.size()-1);
        this.gameCharacters.remove(this.gameCharacters.size()-1);
        this.gameCharacters.remove(this.gameCharacters.size()-1);


        this.imageURLS = this.GetImageUrls(result);

        Log.i("Ches", "Cant personajes " + String.valueOf(this.gameCharacters.size()) );
        Log.i("Ches", "Cant links" + String.valueOf(imageURLS.size()));

        // Se eliminan los ultimos 15 de links
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);
        this.imageURLS.remove(this.imageURLS.size() - 1);

        //Log.i("Ches", "Cant personajes " + String.valueOf(this.gameCharacters.size()) );
        //Log.i("Ches", "Cant links" + String.valueOf(imageURLS.size()));

        //Log.i("Ches", this.imageURLS.toString());
        //Log.i("Ches", this.gameCharacters.toString());
        this.DownloadImage();
        this.RandomButtona();
    }

    public void OnClickAnyButton(View view){

        Button button = (Button) view;
        String buttonText  = (String)button.getText();
        String correctText = this.gameCharacters.get(this.correctAnswerIndex);
        if(buttonText.equals(correctText)){
            Toast toast = Toast.makeText(this,"Correcto",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this,"Incorrecto",Toast.LENGTH_LONG);
            toast.show();
        }


        this.DownloadImage();
        this.RandomButtona();



    }


    public void DownloadImage(){

        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap bitmap = null;
        Random ran = new Random();
        int x4= ran.nextInt(this.imageURLS.size());
        try {
            bitmap = imageDownloader.execute(this.imageURLS.get(x4)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(bitmap != null){
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            this.correctAnswerIndex = x4;
        }


    }




    public ArrayList<String> GetImageUrls(String html){

        int size = html.length();
        char character = ' ';
        String temp = "";
        ArrayList<String> links = new ArrayList<>();
        for(int i = 0 ; i < size ; i++ ){
            character = html.charAt(i);
            if(character == 'h'){

                temp += character;
                i++;

                while( temp.length() < 5){
                    character = html.charAt(i);
                    temp += character;
                    i++;
                }
                Log.i("Ches", "Temp: " + temp);
                if(temp.equals("https")){

                    while(character != '"'){

                        character = html.charAt(i);
                        if(character != '"'){
                            temp += character;
                        }
                        i++;
                    }

                    links.add(temp);
                    temp = "";

                }
                else{
                    temp = "";
                }

            }


        }
        Log.i("Ches",links.toString());
        return links;
    }
    public void RandomButtona() {

        Button button1 = findViewById(R.id.buttonOption1);
        Button button2 = findViewById(R.id.buttonOption2);
        Button button3 = findViewById(R.id.buttonOption3);
        Button button4 = findViewById(R.id.buttonOption4);

        Random ran = new Random();
        int lenList = this.gameCharacters.size();
        int x1 = ran.nextInt(lenList) + 0;
        int x2 = ran.nextInt(lenList) + 0;
        int x3 = ran.nextInt(lenList) + 0;
        int x4= ran.nextInt(lenList) + 0;


        String character1 = this.gameCharacters.get(x1);
        String character2 = this.gameCharacters.get(x2);
        String character3 = this.gameCharacters.get(x3);
        String character4 = this.gameCharacters.get(x4);

        button1.setText(character1);
        button2.setText(character2);
        button3.setText(character3);
        button4.setText(character4);
;
        Random r = new Random();
        int randomCorrect = r.nextInt(4);

        if(randomCorrect == 0){
            String correct = this.gameCharacters.get(this.correctAnswerIndex);
            button1.setText(correct);
        }
        else if(randomCorrect == 1){
            String correct = this.gameCharacters.get(this.correctAnswerIndex);
            button2.setText(correct);
        }
        else if(randomCorrect == 2){
            String correct = this.gameCharacters.get(this.correctAnswerIndex);
            button3.setText(correct);

        }
        else if(randomCorrect == 3){
            String correct = this.gameCharacters.get(this.correctAnswerIndex);
            button4.setText(correct);

        }


    }


    public class DownloadTest extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls){

            URL url;
            HttpsURLConnection urlConnection;
            String complete = "";

            try {

                /*
                //File
                File file = new File("D:\\page.txt");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                */


                //Connection

                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br  = new BufferedReader(inputStreamReader);

                String inputLine;
                boolean addString = false;
                while((inputLine = br.readLine()) != null){

                   if(inputLine.equals("    <ul class=\"editorial\">")){
                       addString = true;
                    }

                    if(addString){
                        complete += inputLine;
                    }

                    //Log.i("Ches", "InicioLinea:" + inputLine+ "FinLinea"+ "\n");
                    //complete += inputLine;
                   // complete += "\n";
                   // bw.write(inputLine);
                    //bw.newLine();
                }
                br.close();





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

           // Log.i("Ches",complete);
            return complete;

        }



    }






    public class ImageDownloader extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


    }

    }

