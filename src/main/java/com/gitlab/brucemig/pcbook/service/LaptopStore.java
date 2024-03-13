package com.gitlab.brucemig.pcbook.service;

import com.gitlab.brucemig.pcbook.pb.Filter;
import com.gitlab.brucemig.pcbook.pb.Laptop;
import io.grpc.Context;

public interface LaptopStore {
    void Save(Laptop laptop) throws Exception;
    Laptop Find(String id);
    void Search(Context ctx, Filter filter, LaptopStream stream);
}
