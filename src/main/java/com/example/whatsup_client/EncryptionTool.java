package com.example.whatsup_client;

import java.util.Random;

//Class that allows us to encrypt messages
public class EncryptionTool {

    //Function that uses symmetric encryption and asymmetric encryption and decryption on a message with the selected key
    //Encryption method: Caesar
    public String encryptMessage(String message, int key){

        char[] messageArray = message.toCharArray();
        String encryptedMessage = "";

        for(char letter: messageArray){

            char encryptedChar = (char) ((int) letter + key);   //Transforms the character into ASCII code, moves it the selected times and returns it to char
            encryptedMessage = encryptedMessage + encryptedChar;    //Adds the encrypted char into the encrypted message

        }
        return encryptedMessage;    //Returns the encrypted message
    }

    //Function that decrypts a symmetric encrypted message
    public String symmetricDecryption(String message, int key){
        char[] messageArray = message.toCharArray();
        String decryptedMessage = "";
        for(char letter: messageArray){
            char encryptedChar = (char) ((int) letter - key);
            decryptedMessage = decryptedMessage + encryptedChar;
        }
        return decryptedMessage;
    }

    //Function that allows to send a signed message
    public String digitalSignature( String message, int key ){
        String hash = Integer.toString(message.hashCode()); //Converts our message to a hash
        String cipheredHash = encryptMessage(hash, key);    //Encrypts our hash
        return cipheredHash + ';' + message;    //Returns the signed document: Encrypted hash and the original message
    }

    //Function that allows to proof the validation of a signed message
    public boolean validateSignature(String signature, int key){

        String[] parts = signature.split(";", 2);

        String decryptedSignature = symmetricDecryption(parts[0], key);
        String hashedMessage =  Integer.toString(parts[1].hashCode());

        if( hashedMessage.equals(decryptedSignature) ){
            return true;
        }

        return false;
    }

    //Function that sends a digital envelope of our message
    public String digitalEnvelope( String message, int privateKey, int publicKey) {

        //Generation of the random key
        Random rand = new Random(); //Allows to generate random numbers
        int randomKey = rand.nextInt(100) + 1;  //Generates the random key from 1 to 100


        //Creates the signature for the message
        String digitalSignature = digitalSignature(message, privateKey);

        //Encryption of the signature
        String[] parts = digitalSignature.split(";", 2);
        parts[0] = encryptMessage(parts[0], randomKey);
        parts[1] = encryptMessage(parts[1], randomKey);
        digitalSignature = parts[0] + ';' + parts[1];

        //Encryption of the generated random key
        String encryptedRandomKey = encryptMessage(Integer.toString(randomKey), publicKey);

        //Returns the envelope: the encrypted random key and the encrypted signature
        return encryptedRandomKey + ';' + digitalSignature;
    }

    public String validateEnvelope(String envelope, int privateKey, int publicKey){

        String[] parts = envelope.split(";", 3);

        //Decryption of the generated random key
        parts[0] = symmetricDecryption(parts[0], privateKey);

        int symmetricDecryptedKey = Integer.parseInt(parts[0]);

        parts[1] = symmetricDecryption(parts[1], symmetricDecryptedKey);
        parts[2] = symmetricDecryption(parts[2], symmetricDecryptedKey);

        boolean validated = validateSignature( parts[1] + ';' + parts[2], publicKey);

        return validated + ";" + parts[2];
    }

}