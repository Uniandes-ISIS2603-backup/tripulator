/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.tripulator.entities;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author josef
 */
@Entity
public class FotoEntity extends BaseEntity implements Serializable{
   
    private String src;
    
    public String getSrc()
    {
        return src;
    }
    
    public void setSrc(String src)
    {
        this.src=src;
    }
    
}
