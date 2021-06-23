package com.ford.chibuisi.springcloudfunctiongcppubsub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PubSubMessage {
    private String data;

    private Map<String, String> attributes;

    private String messageId;

    private String publishTime;
}
