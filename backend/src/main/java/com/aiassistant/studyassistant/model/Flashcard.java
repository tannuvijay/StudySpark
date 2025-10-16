
package com.aiassistant.studyassistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is now a Plain Old Java Object (POJO), not a MongoDB document.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    private String term;
    private String definition;
}
