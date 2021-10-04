/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.codingdojo.students;


import fr.rphstudio.codingdojo.game.Pod;
import fr.rphstudio.codingdojo.game.PodPlugIn;

import java.util.concurrent.TimeUnit;

/**
 * @author Romuald GRIGNON
 */
public class Student99 extends PodPlugIn {
    public Student99(Pod p) {
        super(p);
    }

    //-------------------------------------------------------
    // DECLARE YOUR OWN VARIABLES AND FUNCTIONS HERE


    /*
    --------------------
    VARIABLES GLOBALES
    --------------------
    */


    // Définit une valeur de vitesse de base
    static float speedGlobal = 1;

    static float angle;
    static float angle2;
    // En recharge ou non
    static boolean needRecharge = false;

    static boolean isCharging = false;


     /*
   --------------------
   FONCTIONS UTILITAIRES
   --------------------
    */


    // Définit si le vaisseau charge ou non
    boolean isShipCharging() {
        if (distanceBetweenPositionsFC99() < 0.2f) {
            return true;
        } else return false;
    }

    // Donne position du prochain C.C.P
    int getFirstChargingCheckPointIndex99() {
        int chargeur = 0;
        for (int x = 0; x <= getNbRaceCheckPoints(); x++) {
            if (isCheckPointCharging(x)) {
                chargeur = x;
                x = getNbRaceCheckPoints() + 1;
            }
        }
        return chargeur;
    }



    /*
      void turnTowardPosition99(float x, float y) {
      float ax = x - getShipPositionX();
      float by = y - getShipPositionY();
      double ang = Math.atan2(ay * ay, db * bx);
      this.turnToAngle((float) (ang * 180.0D / 3.141592653589793D));
      }
     */

    // Donne la distance entre deux positions
    float getDistance(float x1, float y1, float x2, float y2) {
        float deltax = x2 - x1;
        float deltay = y2 - y1;
        float distance = sqrt((deltax * deltax) + (deltay * deltay));
        return distance;
    }

    // Calculer l'angle relatif entre le vaisseau et le prochain C.C.P
    float getRelativeAngleDifference99(float a, float b) {
        float relativeAngle = b - a;
        if (relativeAngle < -180) {
            relativeAngle = relativeAngle + 360;
        } else if (relativeAngle > 180) {
            relativeAngle = relativeAngle - 360;
        }
        return relativeAngle;
    }

    // Savoir s'il faut recharger la batterie ou non
    boolean updateChargingMode99(float minBat, float maxBat) {
        if (getShipBatteryLevel() < minBat) {
            needRecharge = true;
            return true;
        } else if (getShipBatteryLevel() < (minBat + 20) && distanceBetweenPositionsFC99() < 0.5f) {
            needRecharge = true;
            return true;
        } else if (getShipBatteryLevel() > maxBat) {
            needRecharge = false;
            return false;
        }
        return needRecharge;
    }

    boolean onCheckPoint() {
        if (distanceBetweenPositionsNC99() < 0.2f) {
            return true;
        }
        return false;
    }


    /*
    --------------------
    CHARGING CHECKPOINTS
    --------------------
     */


    // Se tourne vers le prochain C.C.P
    void turnTowardFirstChargingCheckpoint99() {
        angle = getRelativeAngleDifference99(getShipAngle(),
                AbsoluteAngleFromPositionsFC99());
    }

    float AbsoluteAngleFromPositionsFC99() {
        float checkX = getCheckPointX(getFirstChargingCheckPointIndex99());
        float checkY = getCheckPointY(getFirstChargingCheckPointIndex99());
        float AbsoluteAngleFromPositionsFC = (atan2(
                (checkY - getShipPositionY()), checkX - getShipPositionX()));
        return AbsoluteAngleFromPositionsFC;
    }


    /*
    --------------------
    SIMPLES CHECKPOINTS
    --------------------
     */


    // Se tourne vers le prochain C.P
    void turnTowardNextCheckpoint99() {
        angle = getRelativeAngleDifference99(getShipAngle(), AbsoluteAngleFromPositionsNC99());
    }

    ///////////////////
    float getAbsoluteAngleFromPositions99(float x0, float y0, float x1, float y1) {
        float checkX = x1 - x0;
        float checkY = y1 - y0;
        return atan2(checkY  , checkX);// * (180.0F / 3.1415927F);
    }
////////
    float AbsoluteAngleFromPositionsNC99() {
        float checkX = getCheckPointX(getNextCheckPointIndex());
        float checkY = getCheckPointY(getNextCheckPointIndex());
        return getAbsoluteAngleFromPositions99(checkX, checkY, getShipPositionX(), getShipPositionY());
    }
   /*float AbsoluteAngleFromPositionNC99() {
        float checkX = getCheckPointX(getNextCheckPointIndex());
        float checkY = getCheckPointY(getNextCheckPointIndex());
        return getAbsoluteAngleFromPositions(getShipPositionX(), getShipPositionY(),
                checkX, checkY);*/
float valueangletraj(){
    float x = getAbsoluteAngleFromPositions99(getShipSpeedX(), getShipSpeedY(), getShipPositionX(), getShipPositionY());
    return x;
}

    float angleTrajCP() {
        float x = getAbsoluteAngleFromPositions99(getShipSpeedX(), getShipSpeedY(), getShipPositionX(), getShipPositionY());
        return -1*getRelativeAngleDifference99(x, AbsoluteAngleFromPositionsNC99());
    }

    float angleTrajCP2() {
        return (AbsoluteAngleFromPositionsNC99()-angleTrajCP());
    }

    void compensateTraj() {

        angle = getRelativeAngleDifference99(getShipAngle(), angleTrajCP2());
    }
/////////////////

    int getPreviousCheckPointIndex99() {
        int y = getNextCheckPointIndex();
        int Previous = 0;
        while (onCheckPoint()) {
            for (int x = 0; x < getNbRaceCheckPoints(); x++) {
                Previous = y - 1;
                x = x + 1;
            }
        }
        return Previous;
    }


    @Override

    public void turnTowardPosition(float x, float y) {
        super.turnTowardPosition(x, y);
    }


   /*
    --------------------
    GESTION DE LA VITESSE
    --------------------
    */

/*
    @Override
    public void useBoost() {
        if(useBoost == true ) {
            super.useBoost();
        }
    }
    static boolean useBoost;

    void boost() {

        float boostLevel = getShipBoostLevel();
        if (boostLevel == 100.0f
                && getDistance(getCheckPointX(getNextCheckPointIndex()),getCheckPointY(getNextCheckPointIndex()),
                    getShipPositionX(), getShipPositionY() ) > 5
                && needRecharge == false
                && onCheckPoint()) {
            useBoost = true;
        }else {
            useBoost = false;
        }
    }
*/

    void gestionSpeed() {

        int speedActual = (int) getShipSpeed();
        System.out.println(speedActual);

        if (needRecharge == true && distanceBetweenPositionsFC99() < 0.5f && speedActual < 1f) {
            speedGlobal = -1;
            turnTowardNextCheckpoint99();
            //} else if (needRecharge == false && distanceBetweenPositionsNC99() < 1) {
            //    speedGlobal = 0.05f;
            //} else if (needRecharge == false && distanceBetweenPositionsNC99() < 2) {
            //    speedGlobal = 0.15f;
            //} else if (needRecharge == false && distanceBetweenPositionsNC99() < 4) {
            //    speedGlobal = 0.20f;
            //} else if (needRecharge == false && distanceBetweenPositionsNC99() < 6) {
            //    speedGlobal = 0.35f;
            //} else if (needRecharge == false && distanceBetweenPositionsNC99() < 8) {
            //    speedGlobal = 0.80f;
        } else if (needRecharge == true && distanceBetweenPositionsFC99() < 0.5f) {
            speedGlobal = -1f;
        } else if (needRecharge == true && distanceBetweenPositionsFC99() < 1f) {
            speedGlobal = 0.3f;
        } else {
            speedGlobal = 1;
        }

    }

    // Calculer distance entre vaisseau et prochain C.C.P
    float distanceBetweenPositionsFC99() {
        return getDistance(getCheckPointX(getFirstChargingCheckPointIndex99()), getCheckPointY(getFirstChargingCheckPointIndex99()),
                getShipPositionX(), getShipPositionY());
    }

    // Calculer distance entre vaisseau et prochain C.P
    float distanceBetweenPositionsNC99() {
        return getDistance(getCheckPointX(getNextCheckPointIndex()), getCheckPointY(getNextCheckPointIndex()),
                getShipPositionX(), getShipPositionY());
    }

    // Bouge le vaisseau
    void moveAndRecharge99(float speed, float minBat, float maxBat) {

        gestionSpeed();
        if (updateChargingMode99(20.0f, 85.0f) == true) {
           turnTowardFirstChargingCheckpoint99();
        } else if(valueangletraj()>30){
            compensateTraj();
        }
            else {
            turnTowardNextCheckpoint99();

        }
        turn(angle);
        distanceBetweenPositionsFC99();
        distanceBetweenPositionsNC99();
        //  getAbsoluteAngleFromPositions();
        accelerateOrBrake(speedGlobal);
    }


    // END OF VARIABLES/FUNCTIONS AREA
    //-------------------------------------------------------

    @Override
    public void process(int delta) {
        //-------------------------------------------------------
        // WRITE YOUR OWN CODE HERE

        setPlayerName("</SPOUTNIK.99/>");
        selectShip(25);
        setPlayerColor(51, 153, 204, 255);

        moveAndRecharge99(2f, 25, 90);


        // END OF CODE AREA
        //-------------------------------------------------------
    }

}

/**
         *  GUIDE POUR AMELIORER LE VAISSEAU =


         - Batterie du vaisseau :
         getShipBatteryLevel : savoir si la batterie recharge ou non

         - Pour le DRIFT :
         getSecondCheckPointIndex : Position du C.P d'encore après
         getShipSpeedX & getShipSpeedY : donne vecteur vitesse

         - Pour le BOOST :
         useBoost : Active le boost
         getShipBoostLevel : Voir si le boost est ready

         - Pour réduire la vitesse aux C.P :
         Faire des else if pour les différentes distances entre vaisseau / C.P

         - Pour s'arrêter à la RECHARGE :
         S'orienter vers le prochain C.P pendant la recharge
         Utiliser le BOOST après avoir recharger si la distance n'est pas trop grande et si l'énergie du boost est pleine




         void gestionTurn(){
         if (updateChargingMode99(20.0f, 85.0f) == true) {
         moveToFirstChargingCheckpoint99();
         } else {
         moveToNextCheckpoint99();
         }
         }

 */

//float turnToAngle99() {getRelativeAngleDifference(float angle1, float angle2);}


/*

    // Calculer l'angle absolu entre le vaisseau et le prochain C.P
    float AbsoluteAngleFromPositionFC99() {
        float checkX = getCheckPointX(getFirstChargingCheckPointIndex99());
        float checkY = getCheckPointY(getFirstChargingCheckPointIndex99());
        return absoluteAngle(getShipPositionX(), getShipPositionY(),
                checkX, checkY);
    }

    //Calculer l'angle absolu entre le vaisseau et le prochain C.C.P
    float AbsoluteAngleFromPositionNC99() {
        float checkX = getCheckPointX(getNextCheckPointIndex());
        float checkY = getCheckPointY(getNextCheckPointIndex());
        return absoluteAngle(getShipPositionX(), getShipPositionY(),
                checkX, checkY);


                 float absoluteAngle(float x1,float x2,float y1, float y2){
        float angle= atan2(y2-y1,x2-x1);
        return angle;
    }
    }*/

/*
        float PreviousX = getCheckPointX(getPreviousCheckPointIndex());
         float PreviousY = getCheckPointY(getPreviousCheckPointIndex());

         float NextX = getCheckPointX(getNextCheckPointIndex());
         float NextY = getCheckPointY(getNextCheckPointIndex());
         */

/**
    void boost() {

    if (getShipBoostLevel == 100.0f && distnace avec checkpoint > 5f) {
      useBoost
     }else if (getShipBoostLevel == 100.0f && plusieurs checkpoints alignés) {
       useBoost
     }
    return useBoost;
 }
 */