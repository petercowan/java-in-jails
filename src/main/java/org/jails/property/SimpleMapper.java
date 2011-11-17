package org.jails.property;

import org.jails.property.handler.LoadObjectHandler;
import org.jails.property.handler.SimplePropertyHandler;
import org.jails.property.parser.SimplePropertyParser;

/**
 * <pre>
 * Populate the a bean using its setters and  a map of string values. Map
 * keys correspond to bean property names. Map keys may have nested properties
 * and/or ordered indexes appended to them. Below iare several examples of how
 * property names map to object values.
 *
 * <code>
 * class Person {
 *
 *     public String getName();
 *     public setName(String name);
 *
 *     public Addresss getAddress();
 *     public void getAddress(Addresss address);
 *
 *     public List<Phone> getPhones();
 *     public setPhones(List<Phone> phones);
 * }
 *
 * class Address {
 *     public getZip();
 *     public setZip(String zip);
 * }
 *
 * class Phone {
 *     public getNumber();
 *     public setNumber(String number);
 * }
 *
 * Mapper mapper = new SimpleMapper();
 * Person p = //get Person somehow...;
 *
 * person.name -> person.setName(person.name);
 *
 * person.address.zip ->
 * 		if (person.getAddress() == null) {
 * 		    person.setAddress(new Address())
 * 		}
 * 		person.getAddress().setZip(person.address.zip);
 *
 * Order indexes are used for populating multiple objects in a list.
 *
 * person.phone[0] -> person.getPhones().add(person.phone[0]);
 *
 * You can also map values to multiple Person objects:
 *
 * List<Person> persons = //get list of persons
 *
 *
 * person[0].name -> person.setName(person[0].name);
 * person[0].phone[0] -> person.getPhones().add(person[0].phone[0]);
 * person[1].name -> person.setName(person[1].name);
 * person[1].phone[0] -> person.getPhones().add(person[1].phone[0]);
 *
 * Setting of nested attribute names is restricted by default. In order to enable these,
 * you can do it on a per class, or per property basis using these attributes:
 *
 * see {@link org.jails.property.AcceptsNestedAttributes}
 * see {@link org.jails.property.IgnoreNestedAttributes}
 *
 * AcceptsNestedAttributes can be set at the class level, in which case all nested classes will
 * allow the use of nested properties. To disallow them for individual properties, use the
 * IgnoreNestedAttributes attribute. Alternately, you can just use AcceptsNestedAttributes on a
 * field by field basis.
 *
 * Additionally, SimpleMapper provides for the ability to load a member object based on a nestedProperty
 * name/value pair, but this functionality depends on your persistence API, so in order to use it
 * you must create your own implemenation.
 *
 * see {@link org.jails.property.handler.LoadObjectHandler}
 *
 * Once you have added your own LoadObjectHandler, you can annotate your classes with the IdentifyBy
 * annotation:
 *
 * see {@link IdentifyBy}
 *
 * If a property name points to a member Object that is null, and there is a nestedProperty whose name
 * matches the field value set in the Object's class' IdenityBy, the the member Object will be loaded from the
 * database. ex
 *
 * person.address.id ->
 * 		if (person.getId() == null) {
 * 		    Address address = LoadObjectHandler.getObject(Class classType, String nestedProperty, String[] valArray);
 *	 		person.setAddress(address);
 * 		}
 * </pre>
 *
 * @see org.jails.property.AcceptsNestedAttributes
 * @see org.jails.property.IgnoreNestedAttributes
 * @see org.jails.property.handler.LoadObjectHandler
 * @see IdentifyBy
 */
public class SimpleMapper extends Mapper {
	public SimpleMapper() {
		super(new SimplePropertyParser(), new SimplePropertyHandler());
	}

	public SimpleMapper(LoadObjectHandler loadObjectHandler) {
		super(new SimplePropertyParser(), new SimplePropertyHandler(loadObjectHandler));
	}

}
