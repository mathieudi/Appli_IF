package com.example.mathieu.testmenu2.LineUp;

import android.content.Context;
import android.widget.GridLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mathieu on 19/09/2015.
 */
public class LineUpGridLayout {
    GridLayout _mainGrid;
    List<Show> _showList;
    int hourStart;
    int hourEnd;

    public LineUpGridLayout(List<Show> showList, Context cont) {
        _showList = showList;
        _mainGrid = new GridLayout(cont);

        int nbHours = 24 - hourStart + hourEnd;

        _mainGrid.setColumnCount(5);
        _mainGrid.setRowCount(nbHours*4);

        //Gérer le grid pour afficher une grille (lignes)
        //


    }

    public GridLayout getGrid(){
        return _mainGrid;
    }

    public void setHours (){
        Iterator<Show> showIt = _showList.iterator();

        Show currentShow = showIt.next();
        long minTmp = currentShow._start.getTime();
        long maxTmp = currentShow._start.getTime();

        //Boucle permettant de récupérer le début et la fin des concerts du jour
        //
        while(showIt.hasNext()){
            currentShow = showIt.next();

            if (currentShow._start.getTime() < minTmp){
                minTmp = currentShow._start.getTime();
            }
            if(currentShow._start.getTime() > maxTmp){
                maxTmp = currentShow._start.getTime();
            }
        }

        hourStart = getFromDate(new Date(minTmp), Calendar.HOUR_OF_DAY);
        hourEnd = getFromDate(new Date(maxTmp), Calendar.HOUR_OF_DAY);
    }

    public static int getFromDate(Date date, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

}
