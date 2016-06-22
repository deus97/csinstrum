package com.gorilla.calculation.impl;

import com.gorilla.calculation.StatisticsCalculator;
import com.gorilla.domain.Instrument;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Calculates sum of few newest (with latest date) instruments.
 */
public class FewNewestSummingCalculator implements StatisticsCalculator {

    private int few;

    private Queue<Instrument> fewNewest;

    public void init() {
        //Queue is sorted by Instrument::getDate, head is the oldest instrument.
        fewNewest = new PriorityQueue<>(few, Comparator.comparing(Instrument::getDate));
    }

    /**
     * First fill queue with whatever instruments, after the queue is full insert new instrument
     * only if it's newer than the oldest instrument in the queue. Maintain constant queue size
     * by removing head before inserting new instrument.
     * Head is always the oldest instrument in the queue.
     */
    @Override
    public synchronized void accept(Instrument instrument) {
        if(fewNewest.size() < few) {
            fewNewest.add(instrument);
        } else if(instrument.getDate().isAfter(fewNewest.peek().getDate())) {
            fewNewest.poll();
            fewNewest.add(instrument);
        }
    }

    @Override
    public String getStatisticsAsString() {
        BigDecimal result = BigDecimal.ZERO;

        for(Instrument e : fewNewest) {
            result = result.add(e.getPrice());
        }

        return "sum of " + few + " newest instruments is: " + result;
    }

    public void setFew(int few) {
        this.few = few;
    }
}
