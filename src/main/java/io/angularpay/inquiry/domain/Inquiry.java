
package io.angularpay.inquiry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("inquiries")
public class Inquiry {

    @Id
    private String id;
    @Version
    private int version;
    private String reference;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("last_modified")
    private String lastModified;
    @JsonProperty("inquirer_name")
    private String inquirerName;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private InquirerCategory category;
    private InquiryStatus status;
}
