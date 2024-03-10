package com.gitlab.brucemig.pcbook.service;

import com.gitlab.brucemig.pcbook.pb.Laptop;

public interface LaptopStore {
    void Save(Laptop laptop) throws Exception;
    Laptop Find(String id);
}
