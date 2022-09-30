﻿/*|-----------------------------------------------------------------------------
 *|            This source code is provided under the Apache 2.0 license      --
 *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
 *|                See the project's LICENSE.md for details.                  --
 *|           Copyright (C) 2022 Refinitiv. All rights reserved.            --
 *|-----------------------------------------------------------------------------
 */

using System;

namespace Refinitiv.Eta.Codec
{
    /// <summary>
	/// A signed integer that currently represents a value of up to 63 bits along
	/// with a one bit sign (positive or negative).
	/// </summary>
	sealed public class Int : IEquatable<Int>
	{
		internal long _longValue;

		// for value(String) method
		private string trimmedVal;

        /// <summary>
		/// Creates <see cref="Int"/>.
		/// </summary>
		/// <returns> Int object
		/// </returns>
		/// <seealso cref="Int"/>
		public Int()
		{
		}

        /// <summary>
		/// Sets members in Int to 0.
		/// </summary>
		public void Clear()
		{
			_longValue = 0;
		}

        /// <summary>
        /// This method will perform a deep copy into the passed in parameter's 
        ///          members from the Object calling this method.
        /// </summary>
        /// <param name="dest"> the value getting populated with the values of the calling Object
        /// </param>
        /// <returns> <seealso cref="CodecReturnCode.SUCCESS"/> on success,
        ///         <seealso cref="CodecReturnCode.INVALID_ARGUMENT"/> if the destInt is null.  </returns>
        public CodecReturnCode Copy(Int dest)
		{
			if (null == dest)
			{
				return CodecReturnCode.INVALID_ARGUMENT;
			}

			dest._longValue = _longValue;
			dest.IsBlank = IsBlank;

			return CodecReturnCode.SUCCESS;
		}

        /// <summary>
        /// Is Int blank.
        /// </summary>
        public bool IsBlank { get; internal set; }

        /// <summary>
		/// Set value to int.
		/// </summary>
		/// <param name="value"> the value to set </param>
		public void Value(int value)
		{
			_longValue = value;
            IsBlank = false;
		}

        /// <summary>
		/// Set value to long.
		/// </summary>
		/// <param name="value"> the value to set </param>
		public void Value(long value)
		{
			_longValue = value;
            IsBlank = false;
		}

        /// <summary>
        /// Return value as long.
        /// </summary>
        /// <returns> the value as long </returns>
        public long ToLong()
		{
			return _longValue;
		}

        /// <summary>
		/// Used to encode into a buffer.
		/// </summary>
		/// <param name="iter"> <see cref="EncodeIterator"/> with buffer to encode into. Iterator
		///            should also have appropriate version information set.
		/// </param>
		/// <returns> <sees cref="CodecReturnCode"/>
		/// </returns>
		/// <seealso cref="EncodeIterator"/>
		public CodecReturnCode Encode(EncodeIterator iter)
		{
			if (!IsBlank)
			{
				return Encoders.PrimitiveEncoder.EncodeInt((EncodeIterator)iter, this);
			}
			else
			{
				return CodecReturnCode.INVALID_ARGUMENT;
			}
		}

        /// <summary>
		/// Decode Int.
		/// </summary>
		/// <param name="iter"> <see cref="DecodeIterator"/> with buffer to decode from and
		///            appropriate version information set.
		/// </param>
		/// <returns> <see cref="CodecReturnCode"/>, SUCCESS if success, INCOMPLETE_DATA if
		///         failure, BLANK_DATA if data is blank value
		/// </returns>
		/// <seealso cref="DecodeIterator"/>
		public CodecReturnCode Decode(DecodeIterator iter)
		{
			return Decoders.DecodeInt(iter, this);
		}

        /// <summary>
		/// Convert <seealso cref="Int"/> to a numeric string.
		/// </summary>
		/// <returns> the string representation of this <seealso cref="Int"/> </returns>
		public override string ToString()
		{
			return Convert.ToString(_longValue);
		}

        /// <summary>
		/// Set value to int.
		/// </summary>
		/// <param name="value"> the value to set
		/// </param>
		/// <returns> <seealso cref="CodecReturnCode.SUCCESS"/> on success,
		///         <seealso cref="CodecReturnCode.INVALID_ARGUMENT"/> if value is invalid.  </returns>
		public CodecReturnCode Value(string value)
		{
			if (string.ReferenceEquals(value, null))
			{
				return CodecReturnCode.INVALID_ARGUMENT;
			}

			trimmedVal = value.Trim();
			if (trimmedVal.Length == 0)
			{
				// blank
				Blank();
				return CodecReturnCode.SUCCESS;
			}

			try
			{
				Value(long.Parse(trimmedVal));
				return CodecReturnCode.SUCCESS;
			}
			catch (System.FormatException)
			{
				return CodecReturnCode.INVALID_ARGUMENT;
			}
		}

        /// <summary>
		/// Checks equality of two Int data.
		/// </summary>
		/// <param name="thatInt"> the other Int to compare to this one
		/// </param>
		/// <returns> true if Ints are equal, false otherwise </returns>
		public bool Equals(Int thatInt)
		{
			return ((thatInt != null) && (_longValue == thatInt.ToLong()) && (IsBlank == thatInt.IsBlank));
		}

        /// <summary>Determines whether the specified <c>Object</c> is equal to the current <c>Object</c>.</summary>
        /// <param name="other">The <c>Object</c> to compare with the this <c>Object</c>.</param>
        /// <returns><c>true</c> if the specified <c>Object</c> is equal to the current <c>Object</c>;
        /// otherwise, <c>false</c>.</returns>
        public override bool Equals(object other)
        {
            if (other == null)
                return false;

            if (other is Int)
                return Equals((Int)other);

            return false;
        }

        /// <summary>Serves as a hash function for a particular type.</summary>
        /// <returns>A hash code for the current <c>Object</c>.</returns>
        public override int GetHashCode()
        {
            return ToString().GetHashCode();
        }

        internal void Blank()
        {
            Clear();
            IsBlank = true;
        }
    }

}