package com.devculi.embedded.air_quality.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PreDataSection {
    static List<Row> rows = new ArrayList();
    static float[][] emisstionProb = new float[2][4];
    static float[][] transitionProb = new float[2][2];

    public static String getPredict(int ss) throws FileNotFoundException {
        File data = new File("./NhietDo.txt");
        Scanner reader = new Scanner(data);
        while (reader.hasNextLine()) {
            String q = reader.nextLine();
            Row row = new Row();
            row.setT(Float.parseFloat(q));
            rows.add(row);
        }

        data = new File("./DoAm.txt");
        reader = new Scanner(data);
        int i = 0;
        while (reader.hasNextLine()) {
            String q = reader.nextLine();
            rows.get(i).setH(Float.parseFloat(q));
            i++;
        }

        data = new File("./ThoiTiet.txt");
        reader = new Scanner(data);
        i = 0;
        while (reader.hasNextLine()) {
            String q = reader.nextLine();
            rows.get(i).setStatus(q);
            i++;
        }
        processTransitionProb();
        processEmisstionProb();
        return calculateByViterbi((float) 0.8, (float) 0.2, ss);
    }

    public static float[][] processTransitionProb() {
        int NN = 0, NM = 0, MM = 0, MN = 0;
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).getStatus().compareTo("N") == 0) {
                if (rows.get(i - 1).getStatus().compareTo("N") == 0) {
                    NN++;
                } else {
                    MN++;
                }
            } else {
                if (rows.get(i - 1).getStatus().compareTo("N") == 0) {
                    NM++;
                } else {
                    MM++;
                }
            }
        }
        transitionProb[0][0] = (float) NN / (NN + NM + MN + MM);
        transitionProb[0][1] = (float) NM / (NN + NM + MN + MM);
        transitionProb[1][0] = (float) MN / (NN + NM + MN + MM);
        transitionProb[1][1] = (float) MM / (NN + NM + MN + MM);
        return transitionProb;
    }

    public static float[][] processEmisstionProb() {
        float[][] emisstionData = new float[2][4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                emisstionData[i][j] = 0;
            }
        }
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getStatus().compareTo("N") == 0 && rows.get(i).getT() > 30 && rows.get(i).getH() > 75)
                emisstionData[0][0]++;
            else if (rows.get(i).getStatus().compareTo("N") == 0 && rows.get(i).getT() > 30 && rows.get(i).getH() <= 75)
                emisstionData[0][1]++;
            if (rows.get(i).getStatus().compareTo("N") == 0 && rows.get(i).getT() <= 30 && rows.get(i).getH() > 75)
                emisstionData[0][2]++;
            if (rows.get(i).getStatus().compareTo("N") == 0 && rows.get(i).getT() <= 30 && rows.get(i).getH() <= 75)
                emisstionData[0][3]++;
            if (rows.get(i).getStatus().compareTo("M") == 0 && rows.get(i).getT() > 30 && rows.get(i).getH() > 75)
                emisstionData[1][0]++;
            if (rows.get(i).getStatus().compareTo("M") == 0 && rows.get(i).getT() > 30 && rows.get(i).getH() <= 75)
                emisstionData[1][1]++;
            if (rows.get(i).getStatus().compareTo("M") == 0 && rows.get(i).getT() <= 30 && rows.get(i).getH() > 75)
                emisstionData[1][2]++;
            if (rows.get(i).getStatus().compareTo("M") == 0 && rows.get(i).getT() <= 30 && rows.get(i).getH() <= 75)
                emisstionData[1][3]++;
        }
        int totalN = 0;
        int totalM = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0)
                    totalN += emisstionData[i][j];
                else
                    totalM += emisstionData[i][j];
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0)
                    emisstionProb[i][j] = emisstionData[i][j] / totalN;
                else
                    emisstionProb[i][j] = emisstionData[i][j] / totalM;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(emisstionProb[i][j]);
            }
            System.out.println("\n");
        }
        return emisstionProb;
    }

    public static String calculateByViterbi(float NProp, float MProp, int j) {
        double nang = Math.max(NProp * transitionProb[0][0] * emisstionProb[0][j], MProp * transitionProb[1][0] * emisstionProb[0][j]);
        double mua = Math.max(NProp * transitionProb[0][1] * emisstionProb[1][j] , MProp * transitionProb[1][1] * emisstionProb[1][j]);
        System.out.println("Nang: "+nang);
        System.out.println("Mua: "+mua);
        if(nang > mua) return "Không mưa";
        else return "Mưa";
    }

    public static class Row {
        float t;
        float h;
        String status;

        public float getT() {
            return t;
        }

        public void setT(float t) {
            this.t = t;
        }

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Row{" +
                    "t=" + t +
                    ", h=" + h +
                    ", status=" + status +
                    '}';
        }
    }
}
