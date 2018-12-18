package com.example.jingze.zcryptocurrency.customized

import dagger.android.AndroidInjector
import dagger.internal.Beta
import dagger.internal.Preconditions
import okhttp3.internal.Internal.instance
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class DispatchingAndroidProvider<T> @Inject
internal constructor(
        private val providerFactories: Map<Class<out T>, Provider<AndroidProvider.Factory<out T>>>) {

    fun <M : T> get(modelClass: Class<M>): M {
        maybeGet(modelClass)?.apply { return this }
        throw IllegalArgumentException()
    }

    /**
     * Attempts to perform members-injection on `instance`, returning `true` if
     * successful, `false` otherwise.
     *
     * @throws InvalidProviderBindingException if the injector factory bound for a class does not
     * inject instances of that class
     */
    private fun <M : T> maybeGet(modelClass: Class<M>): M? {
        val factoryProvider = providerFactories[modelClass] ?: return null

        val factory = factoryProvider.get() as AndroidProvider.Factory<T>
        try {
            val provider = Preconditions.checkNotNull<AndroidProvider<T>>(
                    factory.create(), "%s.create(I) should not return null.", factory.javaClass)

            return provider.get() as M
        } catch (e: ClassCastException) {
            throw InvalidProviderBindingException(
                    String.format(
                            "%s does not implement AndroidInjector.Factory<%s>",
                            factory.javaClass.canonicalName, instance.javaClass.getCanonicalName()),
                    e)
        }
    }


    /**
     * Exception thrown if an incorrect binding is made for a [AndroidInjector.Factory]. If you
     * see this exception, make sure the value in your `@ActivityKey(YourActivity.class)` or
     * `@FragmentKey(YourFragment.class)` matches the type argument of the injector factory.
     */
    @Beta
    class InvalidProviderBindingException internal constructor(message: String, cause: ClassCastException) : RuntimeException(message, cause)

    /** Returns an error message with the class names that are supertypes of `instance`.  */
    private fun errorMessageSuggestions(modelClass: Class<T>): String {
        val suggestions = ArrayList<String>()
        for (activityClass in providerFactories.keys) {
            if (activityClass.isInstance(instance)) {
                suggestions.add(activityClass.canonicalName)
            }
        }
        Collections.sort(suggestions)

        return if (suggestions.isEmpty())
            String.format(NO_SUPERTYPES_BOUND_FORMAT, instance.javaClass.getCanonicalName())
        else
            String.format(
                    SUPERTYPES_BOUND_FORMAT, instance.javaClass.getCanonicalName(), suggestions)
    }

    companion object {
        private val NO_SUPERTYPES_BOUND_FORMAT = "No injector factory bound for Class<%s>"
        private val SUPERTYPES_BOUND_FORMAT = "No injector factory bound for Class<%1\$s>. Injector factories were bound for supertypes " + "of %1\$s: %2\$s. Did you mean to bind an injector factory for the subtype?"
    }
}