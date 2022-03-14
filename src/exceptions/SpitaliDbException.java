/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * SpitaliDBException merret me exceptions qe bohen gjate nderveprimit me databazen.
 * 
 * @author Fisnik Zejnullahu
 */
public class SpitaliDbException extends Exception{
    private static final long serialVersionUID = 1L;
    
    private int errorCode = -9999;
    
    public SpitaliDbException(String message){
        super(message);
    }
    
    public SpitaliDbException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
    
    public int getErrorCode(){
        return errorCode;
    }
}
