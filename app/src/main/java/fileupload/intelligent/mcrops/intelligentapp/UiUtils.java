package fileupload.intelligent.mcrops.intelligentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class that shows message dialogs. Needs the caller's context to do so. If
 * there are methods to execute after the yes operation, they are invoked using Java
 * Reflection
 * 

 */
public class UiUtils {

	/**
	 * 
	 * @param message
	 * @param activity
	 */
	public static void showMessage(String message, Activity activity) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pressed) {
				switch (pressed) {
				case DialogInterface.BUTTON_POSITIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setPositiveButton("Ok", dialogClickListener).show();
	}


	/**
	 *
	 * @param message
	 * @param activity
	 * @param reflectiveMethod
	 * @param showNoOption
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void showMessage(String message, final Activity activity,
			final Method reflectiveMethod, boolean showNoOption)
			throws IllegalArgumentException, InvocationTargetException,
			IllegalAccessException {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pressed) {
				switch (pressed) {
				case DialogInterface.BUTTON_POSITIVE:
					if (reflectiveMethod != null)
						try {
							reflectiveMethod.invoke(activity, new Object[] {});
						} catch (Exception e) {
							e.printStackTrace();
						}

					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if (showNoOption) {
			builder.setMessage(message)
					//.setPositiveButton("Ok", dialogClickListener).show()
					.setNegativeButton("OK", dialogClickListener).show()
					.setCancelable(false);
		} else {
			builder.setMessage(message)
					.setNegativeButton("OK", dialogClickListener).show();
		}
	}

	public static void showMessage(String message, final Activity activity,
			final Method reflectiveMethod, final Object[] parameters,
			boolean showNoOption) throws IllegalArgumentException,
			InvocationTargetException, IllegalAccessException {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pressed) {
				switch (pressed) {
				case DialogInterface.BUTTON_POSITIVE:
					if (reflectiveMethod != null)
						try {
							reflectiveMethod.invoke(activity, parameters);
						} catch (Exception e) {
							e.printStackTrace();
						}

					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if (showNoOption) {
			builder.setMessage(message)
					.setPositiveButton("Ok", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		} else {
			builder.setMessage(message)
					.setPositiveButton("Ok", dialogClickListener).show();
		}
	}

	/**
	 * 
	 * @param message
	 * @param activity
	 * @param staticReflectiveMethod
	 * @param invokingReference
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void showMessage(String message, Activity activity,
			final Method staticReflectiveMethod, final Object invokingReference)
			throws IllegalArgumentException, InvocationTargetException,
			IllegalAccessException {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pressed) {
				switch (pressed) {
				case DialogInterface.BUTTON_POSITIVE:
					if (staticReflectiveMethod != null)
						try {
							staticReflectiveMethod.invoke(invokingReference,
									new Object[] {});
						} catch (Exception e) {
							e.printStackTrace();
						}

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setPositiveButton("Ok", dialogClickListener).show();
	}

	/**
	 * 
	 * @param message
	 * @param activity
	 * @param reflectiveMethod
	 * @param invokingReference
	 * @param methodParameters
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void showMessage(String message, Activity activity,
			final Method reflectiveMethod, final Object invokingReference,
			final Object[] methodParameters) throws IllegalArgumentException,
			InvocationTargetException, IllegalAccessException {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pressed) {
				switch (pressed) {
				case DialogInterface.BUTTON_POSITIVE:
					if (reflectiveMethod != null && invokingReference != null)
						try {
							reflectiveMethod.invoke(invokingReference,
									methodParameters);
						} catch (Exception e) {
							e.printStackTrace();
						}

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message)
				.setPositiveButton("Ok", dialogClickListener).show();
	}
}
