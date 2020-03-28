package com.nidotim.lottery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nidotim.lottery.model.converter.ListToJsonConverter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Number extends BaseEntity {

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_id", updatable = false)
  @JsonIgnore
  private Ticket ticket;

  @Convert(converter = ListToJsonConverter.class)
  @Builder.Default
  private List<Integer> numbers = new ArrayList<>();

  @Builder.Default
  private boolean scanned = false;

  @Builder.Default
  private boolean win = false;

  @Builder.Default
  private int matched = 0;

}
