package com.gitlab.brucemig.pcbook.service;

public interface RatingStore {
    Rating Add(String laptopID, double score);
}
