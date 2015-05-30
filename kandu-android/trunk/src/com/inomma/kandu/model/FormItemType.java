package com.inomma.kandu.model;

public enum FormItemType {
	TEXT("text"), CHOICE("choice"), NUMBER("number"), MULTICHOICE(
			"multi-choice"), GPS("coordinates"), IMAGE("file"), FOREIGN_KEY(
			"foreign-key"),MANY_TO_MANY("many-to-many"),DECIMAL("decimal"),BOOLEAN("boolean"),DATE("date"),IDFIELD("id-field");

	private String key;

	private FormItemType(String key) {
		this.key = key;
	}

	public static FormItemType getWithKey(String key) {
		for (FormItemType itemType : FormItemType.values()) {

			if (itemType.key.equals(key)) {
				return itemType;
			}
		}
		throw new RuntimeException("Form item type not found with key "+key);
	}
}
