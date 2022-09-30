﻿/*|-----------------------------------------------------------------------------
 *|            This source code is provided under the Apache 2.0 license      --
 *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
 *|                See the project's LICENSE.md for details.                  --
 *|           Copyright (C) 2022 Refinitiv. All rights reserved.              --
 *|-----------------------------------------------------------------------------
 */

using Refinitiv.Eta.Codec;
using System.Text;

namespace Refinitiv.Eta.ValueAdd.Rdm
{
    /// <summary>
    /// Base class for RDM message classes.
    /// </summary>
    public abstract class MsgBase
    {
        private StringBuilder stringBuf = new StringBuilder();
        protected const string eol = "\n";
        protected const string tab = "\t";

        /// <summary>
        /// The ID of the stream the message is sent to.
        /// </summary>
        public virtual int StreamId { get; set; }
        /// <summary>
        /// The domain type of the message.
        /// </summary>
        public virtual int DomainType { get; set; }
        /// <summary>
        /// Message Class <see cref="MsgClasses"/>
        /// </summary>
        public virtual int MsgClass { get; }

        /// <summary>
        /// Clears current message instance.
        /// </summary>
        public virtual void Clear()
        {
            StreamId = 0;
        }

        /// <summary>
        /// Encode an RDM message.
        /// </summary>
        /// <param name="encIter"><see cref="EncodeIterator"/> instance that performs the encoding.</param>
        /// <returns><see cref="CodecReturnCode"/> value indicating the status of the operation.</returns>
        public abstract CodecReturnCode Encode(EncodeIterator encIter);

        /// <summary>
        /// Decode a ETA message into an RDM message.
        /// </summary>
        /// <param name="encIter"><see cref="DecodeIterator"/> instance that performs the decoding.</param>
        /// <param name="msg">Partially decoded message.</param>
        /// <returns><see cref="CodecReturnCode"/> indicating the status of the operation</returns>
        public abstract CodecReturnCode Decode(DecodeIterator encIter, Msg msg);

        protected virtual StringBuilder PrepareStringBuilder()
        {
            stringBuf.Clear();

            stringBuf.Append(tab);
            stringBuf.Append("streamId: ");
            stringBuf.Append(StreamId);
            stringBuf.Append(eol);

            return stringBuf;
        }
    }
}
