package nz.co.greenjersey.inventorytracker.Util;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nz.co.greenjersey.inventorytracker.Misc.RoundUp;
import nz.co.greenjersey.inventorytracker.R;

/**
 * Created by james on 13/04/2018.
 */

public class ListenList<Bike> extends ArrayList<Bike>{
    ArrayList<Bike> list;
    RoundUp roundUp;

    public ListenList(RoundUp roundUp){
        this.roundUp = roundUp;
        list = new ArrayList<>();
    }

    @Override
    public boolean add(Bike bike){
        list.add(bike);
        int index = list.indexOf(bike);
        roundUp.displayData(index);
        Log.d("test list", bike.toString());
        return true;
    }
    @Override
    public Bike get(int index){
        if(!list.isEmpty()){
            return list.get(index);
        }
        return null;
    }
}
