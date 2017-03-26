package fxwindows.core;

/**
 * @author Coen Boelhouwers
 */
public enum LayoutBehavior {
	/**Matches this shape's size to the maximum space allowed by its parent.*/
	FILL_SPACE,
	/**Take up only as much space as wanted by this shape, ignoring maximum bounds*/
	WRAP_CONTENT,
	/**Take up only as much space as wanted by this shape, respecting maximum bounds*/
	WRAP_CONTENT_TILL_MAX
}
