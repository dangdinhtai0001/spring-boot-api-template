package com.phoenix.domain.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UrlAntMatcherModel {
    private String name;
    private String url;
    private String [] permissions;
}
