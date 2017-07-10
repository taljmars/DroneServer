# Exported Services

When developing a Server and Client based on SOAP, we need to be able to instantiate a similar class to the one exist in the server.
Why so? because the server source is not always available. In general, it is the client responsibility to check the classes available in th server via WSImport.
In order to simplify the work, one can use the startModule.sh script (Linux) in order to generate the relevant services as a jar for client use.
Mind that the server must be up and running in order to do so.
On can notice that the class content is almost empty, reason: classes logic is not being available for client use, but only fields.
