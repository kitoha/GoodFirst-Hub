package com.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public record IssueDto (
    String id,
    String title,
    String url,
    String issuer,
    int commentCount,
    int reactionCount,
    int issueNumber,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<LabelDto> labelDtos
){

}
