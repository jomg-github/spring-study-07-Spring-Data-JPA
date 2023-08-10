package study.springdatajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface NameOnly {
    @Value("#{target.name + ' ----- ' + target.createdDate}")
    String getName();
}
