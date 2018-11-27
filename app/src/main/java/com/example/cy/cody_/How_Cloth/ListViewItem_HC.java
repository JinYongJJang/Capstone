package com.example.cy.cody_.How_Cloth;

public class ListViewItem_HC {
    private int ID;
    private String Top;
    private String Bottom;
    private String Coat;

    public int getID(){
        return ID;
    }
    public String getTop(){
        return Top;
    }

    public String getBottom(){
        return Bottom;
    }

    public String getCoat(){
        return Coat;
    }

    public ListViewItem_HC(int id, String top, String bottom, String coat){
        this.ID = id;
        this.Top = top;
        this.Bottom = bottom;
        this.Coat = coat;
    }
}
