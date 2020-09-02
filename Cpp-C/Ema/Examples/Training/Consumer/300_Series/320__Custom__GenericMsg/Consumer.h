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
#include <sys/timeb.h>
#include <time.h>
#else
#include <sys/time.h>
#endif

#include "Ema.h"

unsigned long long getCurrentTime()
{
	unsigned long long msec = 0;
#ifdef WIN32
	struct	_timeb	_time;
	_ftime_s( &_time );
	msec = _time.time*1000 + _time.millitm;
#else
	struct  timeval _time;
	gettimeofday( &_time, 0 );
	msec = ((unsigned long long)(_time.tv_sec))*1000ULL + ((unsigned long long)(_time.tv_usec)) / 1000ULL;
#endif
	return msec;
}

// application defined client class for receiving and processing of item messages
class AppClient : public rtsdk::ema::access::OmmConsumerClient		// client receiving messages
{
public :

	AppClient();

	void decode( const rtsdk::ema::access::Msg& );					// print content of passed in Msg to screen

	void decode( const rtsdk::ema::access::GenericMsg& );			// print content of passed in GenericMsg to screen

	void decode( const rtsdk::ema::access::FieldList& );			// print content of passed in FieldList to screen

	void decode( const rtsdk::ema::access::ElementList& );			// print content of passed in ElementList to screen

	void setOmmConsumer( rtsdk::ema::access::OmmConsumer& );

protected :

	void onRefreshMsg( const rtsdk::ema::access::RefreshMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	void onUpdateMsg( const rtsdk::ema::access::UpdateMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	void onStatusMsg( const rtsdk::ema::access::StatusMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	void onGenericMsg( const rtsdk::ema::access::GenericMsg&, const rtsdk::ema::access::OmmConsumerEvent& );

	rtsdk::ema::access::OmmConsumer* _pOmmConsumer;
	
	rtsdk::ema::access::UInt64 count;
};

#endif // __ema_consumer_h_
