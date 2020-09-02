///*|-----------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      --
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  --
// *|           Copyright (C) 2019 Refinitiv. All rights reserved.            --
///*|-----------------------------------------------------------------------------

#ifndef __ema_consumer_h_
#define __ema_consumer_h_

#include <iostream>

#ifdef WIN32
#include <windows.h>
#else
#include <sys/time.h>
#endif

#include "Ema.h"

void sleep( int millisecs )
{
#if defined WIN32
	::Sleep( (DWORD)(millisecs) );
#else
	struct timespec sleeptime;
	sleeptime.tv_sec = millisecs / 1000;
	sleeptime.tv_nsec = (millisecs % 1000) * 1000000;
	nanosleep( &sleeptime, 0 );
#endif
}

// application defined client class for receiving and processing of item messages
class AppClient : public rtsdk::ema::access::OmmConsumerClient
{
public :

	void decode( const rtsdk::ema::access::RefreshMsg& );			// print content of passed in RefreshMsg to screen

	void decode( const rtsdk::ema::access::UpdateMsg& );			// print content of passed in UpdateMsg to screen
	
	void decode( const rtsdk::ema::access::StatusMsg& );			// print content of passed in StatusMsg to screen

	void decode( const rtsdk::ema::access::GenericMsg& );			// print content of passed in GenericMsg to screen

	void decode( const rtsdk::ema::access::Attrib& );				// print content of passed in Attrib to screen

	void decode( const rtsdk::ema::access::Payload& );				// print content of passed in Payload to screen

	void decode( const rtsdk::ema::access::FieldList& );			// print content of passed in FieldList to screen

	void decode( const rtsdk::ema::access::ElementList& );			// print content of passed in ElementList to screen

	void setOmmConsumer( rtsdk::ema::access::OmmConsumer& );

protected :

	void onRefreshMsg( const rtsdk::ema::access::RefreshMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	void onUpdateMsg( const rtsdk::ema::access::UpdateMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	void onStatusMsg( const rtsdk::ema::access::StatusMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	void onGenericMsg( const rtsdk::ema::access::GenericMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	rtsdk::ema::access::OmmConsumer* _pOmmConsumer;
};

#endif // __ema_consumer_h_
