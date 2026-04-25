package com.example.bolalarakademiyasi.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class IdList {
    private List<UUID> idList;
}
