/**
 * name: messages.js
 * javascript dependencies: PageBus(1.2+)
 * description: Describes the application's message subject, subcription and publishing semantics, as well
 * as all of the messages published by the user interface.
 */

/**
 * Web appplication message facade.
 */
if (MessageBus == null) var MessageBus = {};

/**
 * The message bus implementation.
 */
MessageBus.messageBusProvider = window.PageBus;

/**
 * Publish a message that is managed by the client application's message bus.
 * @param subject The message subject. For example,
 * eg.customer.onSelect is an appropriate subject name, but not link.click.
 * @param message The message.
 */
MessageBus.publish = function(subject, message)
{
    // use tibco message page bus publishing method.
    MessageBus.messageBusProvider.publish(subject, message);
};

/**
 * Subcribe to a message that is managed by the client application's message bus.
 * @param subject The message subject. For example,
 * eg.customer.onSelect is an appropriate subject name, but not link.click.
 * @param scope The scope object for the callback to be invoked on.
 * @param callback The message callback.
 * @return subscription
 */
MessageBus.subscribe = function(subject, scope, callback)
{
    // use tibco message page bus subscribe method.
    return MessageBus.messageBusProvider.subscribe(subject, scope, callback, null);
};

/**
 * The unsubscribe operation cancels a subscription, using the Subscription object
 * returned by MessageBus.subscribe as a handle to the subscription.
 * @param subscription the subscription returned by MessageBus.subscribe.
 */
MessageBus.unsubscribe = function(subscription)
{
    // use tibco message page bus unsubscribe method.
    MessageBus.messageBusProvider.unsubscribe(subscription);
};

/**
 *
 * @param list
 * pop all the subscriptions off an array list and unsubscribe them
 */
MessageBus.unsubscribeList =function(list){
    while (list.length > 0)
       this.unsubscribe(list.pop());
};



