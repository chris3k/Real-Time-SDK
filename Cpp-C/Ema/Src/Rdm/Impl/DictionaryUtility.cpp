/*|-----------------------------------------------------------------------------
 *|            This source code is provided under the Apache 2.0 license      --
 *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
 *|                See the project's LICENSE.md for details.                  --
 *|           Copyright (C) 2019 Refinitiv. All rights reserved.            --
 *|-----------------------------------------------------------------------------
*/

#include "DictionaryUtility.h"
#include "FieldListDecoder.h"
#include "ExceptionTranslator.h"
#include "OmmInvalidUsageException.h"

using namespace rtsdk::ema::access;
using namespace rtsdk::ema::rdm;

DictionaryUtility::DictionaryUtility()
{
}


DictionaryUtility::~DictionaryUtility()
{
}

const DataDictionary& DictionaryUtility::dataDictionary(const rtsdk::ema::access::FieldList& fieldList)
{
	if (!fieldList.hasDecoder())
	{
		throwIueException( "Failed to extract DataDictionary from the passed in FieldList", OmmInvalidUsageException::InvalidArgumentEnum );
	}

	Decoder& decoder = const_cast<rtsdk::ema::access::FieldList&>(fieldList).getDecoder();

	return static_cast<rtsdk::ema::access::FieldListDecoder&>(decoder).getDataDictionary();
}

