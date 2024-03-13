package com.gitlab.brucemig.pcbook.service;

import com.gitlab.brucemig.pcbook.pb.Laptop;

public interface LaptopStream {
    void Send(Laptop laptop);
}
