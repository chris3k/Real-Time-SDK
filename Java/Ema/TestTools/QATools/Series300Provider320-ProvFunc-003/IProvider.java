///*|-----------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      --
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  --
// *|           Copyright (C) 2019 Refinitiv. All rights reserved.            --
///*|-----------------------------------------------------------------------------

//APIQA this file is QATools standalone. See qa_readme.txt for details about this tool.

package com.rtsdk.ema.examples.training.iprovider.series300.ex320_Custom_GenericMsg;

import com.rtsdk.ema.access.EmaFactory;
import com.rtsdk.ema.access.FieldList;
import com.rtsdk.ema.access.GenericMsg;
import com.rtsdk.ema.access.Msg;
import com.rtsdk.ema.access.OmmException;
import com.rtsdk.ema.access.OmmIProviderConfig;
import com.rtsdk.ema.access.OmmProvider;
import com.rtsdk.ema.access.OmmProviderClient;
import com.rtsdk.ema.access.OmmProviderEvent;
import com.rtsdk.ema.access.OmmReal;
import com.rtsdk.ema.access.OmmState;
import com.rtsdk.ema.access.PostMsg;
import com.rtsdk.ema.access.RefreshMsg;
import com.rtsdk.ema.access.ReqMsg;
import com.rtsdk.ema.access.Series;
import com.rtsdk.ema.access.StatusMsg;
import com.rtsdk.ema.rdm.DataDictionary;
import com.rtsdk.ema.rdm.EmaRdm;

class AppClient implements OmmProviderClient
{
    public long itemHandle = 0;
    public DataDictionary dataDictionary = EmaFactory.createDataDictionary();

    private int fragmentationSize = 96000;
    private Series series = EmaFactory.createSeries();
    private RefreshMsg refreshMsg = EmaFactory.createRefreshMsg();
    private int currentValue;
    private boolean result;

    public void onReqMsg(ReqMsg reqMsg, OmmProviderEvent event)
    {
        switch (reqMsg.domainType())
        {
            case EmaRdm.MMT_LOGIN:
                processLoginRequest(reqMsg, event);
                break;
            case EmaRdm.MMT_DICTIONARY:
                processDictionaryRequest(reqMsg, event);
                break;
            case EmaRdm.MMT_MARKET_PRICE:
                processMarketPriceRequest(reqMsg, event);
                break;
            default:
                processInvalidItemRequest(reqMsg, event);
                break;
        }
    }

    public void onReissue(ReqMsg reqMsg, OmmProviderEvent event)
    {
        switch (reqMsg.domainType())
        {
            case EmaRdm.MMT_DICTIONARY:
                processDictionaryRequest(reqMsg, event);
                break;
            default:
                processInvalidItemRequest(reqMsg, event);
                break;
        }
    }

    public void onRefreshMsg(RefreshMsg refreshMsg, OmmProviderEvent event)
    {
    }

    public void onStatusMsg(StatusMsg statusMsg, OmmProviderEvent event)
    {
    }

    public void onGenericMsg(GenericMsg genericMsg, OmmProviderEvent event)
    {
    }

    public void onPostMsg(PostMsg postMsg, OmmProviderEvent event)
    {
    }

    public void onClose(ReqMsg reqMsg, OmmProviderEvent event)
    {
    }

    public void onAllMsg(Msg msg, OmmProviderEvent event)
    {
    }

    void processLoginRequest(ReqMsg reqMsg, OmmProviderEvent event)
    {
        event.provider().submit(refreshMsg.clear().domainType(EmaRdm.MMT_LOGIN).name(reqMsg.name()).nameType(EmaRdm.USER_NAME).complete(true).solicited(true)
                                        .state(OmmState.StreamState.OPEN, OmmState.DataState.OK, OmmState.StatusCode.NONE, "Login accepted"), event.handle());
    }

    void processDictionaryRequest(ReqMsg reqMsg, OmmProviderEvent event)
    {
        result = false;
        refreshMsg.clear().clearCache(true);
        try
        {
            if (reqMsg.name().equals("RWFFld"))
            {
                currentValue = dataDictionary.minFid();

                while (!result)
                {
                    currentValue = dataDictionary.encodeFieldDictionary(series, currentValue, reqMsg.filter(), fragmentationSize);

                    result = currentValue == dataDictionary.maxFid() ? true : false;
                    // APIQA
                    event.provider().submit(refreshMsg.name(reqMsg.name()).serviceId(5).domainType(EmaRdm.MMT_DICTIONARY).filter(reqMsg.filter()).payload(series).complete(result).solicited(true),
                                            event.handle());
                    // END APIQA
                    refreshMsg.clear();
                }
            }
            else if (reqMsg.name().equals("RWFEnum"))
            {
                currentValue = 0;

                while (!result)
                {
                    currentValue = dataDictionary.encodeEnumTypeDictionary(series, currentValue, reqMsg.filter(), fragmentationSize);

                    result = currentValue == dataDictionary.enumTables().size() ? true : false;
                    // APIQA
                    event.provider().submit(refreshMsg.name(reqMsg.name()).serviceId(5).domainType(EmaRdm.MMT_DICTIONARY).filter(reqMsg.filter()).payload(series).complete(result).solicited(true),
                                            event.handle());
                    // END APIQA
                    refreshMsg.clear();
                }
            }
        }
        catch (OmmException excp)
        {
            System.out.println(excp.getMessage());
        }
    }

    void processMarketPriceRequest(ReqMsg reqMsg, OmmProviderEvent event)
    {
        if (itemHandle != 0)
        {
            processInvalidItemRequest(reqMsg, event);
            return;
        }

        FieldList fieldList = EmaFactory.createFieldList();
        fieldList.add(EmaFactory.createFieldEntry().real(22, 3990, OmmReal.MagnitudeType.EXPONENT_NEG_2));
        fieldList.add(EmaFactory.createFieldEntry().real(25, 3994, OmmReal.MagnitudeType.EXPONENT_NEG_2));
        fieldList.add(EmaFactory.createFieldEntry().real(30, 9, OmmReal.MagnitudeType.EXPONENT_0));
        fieldList.add(EmaFactory.createFieldEntry().real(31, 19, OmmReal.MagnitudeType.EXPONENT_0));

        event.provider().submit(refreshMsg.clear().name(reqMsg.name()).serviceName(reqMsg.serviceName()).solicited(true)
                                        .state(OmmState.StreamState.OPEN, OmmState.DataState.OK, OmmState.StatusCode.NONE, "Refresh Completed").payload(fieldList).complete(true), event.handle());

        itemHandle = event.handle();
    }

    void processInvalidItemRequest(ReqMsg reqMsg, OmmProviderEvent event)
    {
        event.provider().submit(EmaFactory.createStatusMsg().name(reqMsg.name()).serviceName(reqMsg.serviceName())
                                        .state(OmmState.StreamState.CLOSED, OmmState.DataState.SUSPECT, OmmState.StatusCode.NOT_FOUND, "Item not found"), event.handle());
    }
}

public class IProvider
{
    public static void main(String[] args)
    {
        OmmProvider provider = null;
        try
        {
            AppClient appClient = new AppClient();
            appClient.dataDictionary.loadFieldDictionary("RDMFieldDictionary");
            appClient.dataDictionary.loadEnumTypeDictionary("enumtype.def");
            provider = EmaFactory.createOmmProvider(EmaFactory.createOmmIProviderConfig().adminControlDictionary(OmmIProviderConfig.AdminControl.USER_CONTROL), appClient);
            while (appClient.itemHandle == 0)
                Thread.sleep(1000);
            FieldList fieldList = EmaFactory.createFieldList();
            for (int i = 0; i < 60; i++)
            {
                fieldList.clear();
                fieldList.add(EmaFactory.createFieldEntry().real(22, 3991 + i, OmmReal.MagnitudeType.EXPONENT_NEG_2));
                fieldList.add(EmaFactory.createFieldEntry().real(30, 10 + i, OmmReal.MagnitudeType.EXPONENT_0));
                provider.submit(EmaFactory.createUpdateMsg().payload(fieldList), appClient.itemHandle);

                Thread.sleep(1000);
            }
        }
        catch (InterruptedException | OmmException excp)
        {
            System.out.println(excp.getMessage());
        }
        finally
        {
            if (provider != null)
                provider.uninitialize();
        }
    }
}
//END APIQA
