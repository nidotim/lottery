package com.nidotim.lottery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nidotim.lottery.model.converter.ListToJsonConverter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotteryHistory extends BaseEntity {

  private Instant date;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lottery_id", updatable = false)
  @JsonIgnore
  private Lottery lottery;

  @Convert(converter = ListToJsonConverter.class)
  @Builder.Default
  private List<Integer> numbers = new ArrayList<>();


}
