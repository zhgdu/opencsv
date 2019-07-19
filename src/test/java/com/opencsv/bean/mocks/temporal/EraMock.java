package com.opencsv.bean.mocks.temporal;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.chrono.*;

public class EraMock {
    @CsvBindByPosition(position = 0)
    @CsvDate("G")
    private IsoEra isoEra;

    @CsvBindByPosition(position = 1)
    @CsvDate(value = "G", chronology = "Hijrah-umalqura")
    private HijrahEra hijrahEra;

    @CsvBindByPosition(position = 2)
    @CsvDate(value = "G", chronology = "Japanese")
    private JapaneseEra japaneseEra;

    @CsvBindByPosition(position = 3)
    @CsvDate(value = "G", chronology = "Minguo")
    private MinguoEra minguoEra;

    @CsvBindByPosition(position = 4)
    @CsvDate(value = "GGGGG", chronology = "ThaiBuddhist")
    private ThaiBuddhistEra thaiBuddhistEra;

    public IsoEra getIsoEra() {
        return isoEra;
    }

    public void setIsoEra(IsoEra isoEra) {
        this.isoEra = isoEra;
    }

    public HijrahEra getHijrahEra() {
        return hijrahEra;
    }

    public void setHijrahEra(HijrahEra hijrahEra) {
        this.hijrahEra = hijrahEra;
    }

    public JapaneseEra getJapaneseEra() {
        return japaneseEra;
    }

    public void setJapaneseEra(JapaneseEra japaneseEra) {
        this.japaneseEra = japaneseEra;
    }

    public MinguoEra getMinguoEra() {
        return minguoEra;
    }

    public void setMinguoEra(MinguoEra minguoEra) {
        this.minguoEra = minguoEra;
    }

    public ThaiBuddhistEra getThaiBuddhistEra() {
        return thaiBuddhistEra;
    }

    public void setThaiBuddhistEra(ThaiBuddhistEra thaiBuddhistEra) {
        this.thaiBuddhistEra = thaiBuddhistEra;
    }
}
